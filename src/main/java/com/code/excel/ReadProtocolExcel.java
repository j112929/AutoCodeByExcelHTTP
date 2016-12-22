/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.excel;

import com.code.model.Controller;
import com.code.model.Module;
import com.code.model.MyClass;
import com.code.model.Property;
import com.code.model.Protocol;
import com.code.model.TO;
import com.code.util.ExcelUtil;
import com.code.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadProtocolExcel
{
  private static final Logger logger = LoggerFactory.getLogger("ReadProtocolExcel");
  public static final String REQUEST_AND_RESPONSE_SHEET_NAME = "请求响应";
  public static final String STRUCT_SHEET_NAME = "数据结构";
  private static final String STRUCT_TYPE = "object";
  private static boolean logValue = false;
  private static final int FIRST_COLUMN_INDEX = 5;

  public static Map<String, Module> readProtocolExcel(String fileName, String sheetName)
  {
    Map moduleMap = new LinkedHashMap();

    Module currentModule = null;
    String moduleNote = "";
    String moduleName = "";
    String note = "";
    String name = "";

    Controller currentController = null;
    String controllerNote = "";
    String controllerName = "";
    try
    {
      File file = new File(fileName);
      InputStream is = new FileInputStream(file);

      XSSFWorkbook workbook = new XSSFWorkbook(is);

      XSSFSheet sheet = workbook.getSheet(sheetName);

      int rowNum = sheet.getLastRowNum();
      logger.debug("文件【】, sheetName【{}】, 总行数【】", new Object[] { fileName, sheet.getSheetName(), Integer.valueOf(rowNum) });

      for (int r = 1; r < rowNum; r += 6)
      {
        XSSFRow firstRow = sheet.getRow(r);
        XSSFCell cellModule = firstRow.getCell(0);
        XSSFCell cellController = firstRow.getCell(1);
        XSSFCell cellTO = firstRow.getCell(2);

        if (cellModule == null) {
          logger.debug("读到一个空cell...");
          break;
        }

        cellModule.setCellType(1);
        String valueModule = cellModule.getStringCellValue().trim();
        cellController.setCellType(1);
        String valueController = cellController.getStringCellValue().trim();
        cellTO.setCellType(1);
        String valueTO = cellTO.getStringCellValue().trim();

        if (!("".equals(valueModule))) {
          String[] ss = splitName(valueModule);
          moduleNote = ss[0];
          moduleName = ss[1];

          currentModule = (Module)moduleMap.get(moduleName);
          if (currentModule == null) {
            currentModule = new Module();
            moduleMap.put(moduleName, currentModule);
          }

          currentModule.setNote(moduleNote);
          currentModule.setName(moduleName);

          logger.debug("模块中文名【{}】, 英文名【{}】", new Object[] { moduleNote, moduleName });
        }

        if (!("".equals(valueController))) {
          String[] ss = splitName(valueController);
          controllerNote = ss[0];
          controllerName = ss[1];

          if (!(currentModule.containController(controllerName))) {
            currentController = new Controller();
            currentModule.getControllerList().add(currentController);
          }

          currentController.setNote(controllerNote);
          currentController.setName(controllerName);
          currentController.setModuleName(moduleName);

          logger.debug("控制器中文名【{}】, 英文名【{}】", new Object[] { controllerNote, controllerName });
        }

        if ("".equals(valueTO)) {
          continue;
        }
        TO to = new TO();
        Protocol request = new Protocol();
        Protocol response = new Protocol();

        String[] ss = splitName(valueTO);
        note = ss[0];
        name = ss[1];

        request.setNote(note);
        request.setName(name);
        request.setModuleName(moduleName);
        request.setProtocolType("Request");
        request.setController(currentController);

        response.setNote(note);
        response.setName(name);
        response.setModuleName(moduleName);
        response.setProtocolType("Response");
        response.setController(currentController);

        to.setName(name);
        to.setNote(note);
        to.setRequest(request);
        to.setController(currentController);
        to.setResponse(response);
        to.setModuleName(moduleName);

        currentController.getToList().add(to);
        logger.debug("类注释【{}】, 类名【{}】", new Object[] { note, name });

        XSSFRow row1 = sheet.getRow(r);
        XSSFRow row2 = sheet.getRow(r + 1);
        XSSFRow row3 = sheet.getRow(r + 2);
        XSSFRow row4 = sheet.getRow(r + 3);
        XSSFRow row5 = sheet.getRow(r + 4);
        XSSFRow row6 = sheet.getRow(r + 5);

        createTO(request, workbook, row1, row2, row3);

        createTO(response, workbook, row4, row5, row6);

        logger.debug("读取到一个通信协议类【{}】", new Object[] { to.toString() });
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.error("出错之前读取的 ，sheetName【{}】, 模块中文名【{}】,模块英文名【{}】，类注释【{}】, 类名【{}】", new Object[] { sheetName, moduleNote, moduleName, note, name });
    }

    return moduleMap;
  }

  private static void createTO(MyClass myClass, XSSFWorkbook workbook, XSSFRow row1, XSSFRow row2, XSSFRow row3)
  {
    int columnNum = row1.getLastCellNum();

    for (int c = 5; c < columnNum; ++c)
    {
      XSSFCell cell1 = row1.getCell(c);
      XSSFCell cell2 = row2.getCell(c);
      XSSFCell cell3 = row3.getCell(c);

      if (cell1 == null) {
        logger.debug("读到一个空cell...");
        return;
      }

      cell1.setCellType(1);
      String value1 = cell1.getStringCellValue().trim();

      if ("".equals(value1)) {
        logger.debug("读到一行的末尾...");
        return;
      }

      getPropertyWithLink(myClass, workbook, cell1, cell2, cell3);
    }
  }

  public static List<MyClass> readStructExcel(String fileName, String sheetName)
  {
    List list = new ArrayList();

    String note = "";
    String name = "";
    try
    {
      File file = new File(fileName);
      InputStream is = new FileInputStream(file);

      XSSFWorkbook workbook = new XSSFWorkbook(is);

      XSSFSheet sheet = workbook.getSheet(sheetName);

      int rowNum = sheet.getLastRowNum();

      logger.debug("文件【】, sheetName【{}】, 总行数【】", new Object[] { fileName, sheet.getSheetName(), Integer.valueOf(rowNum) });

      for (int r = 1; r < rowNum; r += 3)
      {
        MyClass myClass = new MyClass();

        XSSFRow row1 = sheet.getRow(r);
        XSSFRow row2 = sheet.getRow(r + 1);
        XSSFRow row3 = sheet.getRow(r + 2);

        int columnNum = row1.getLastCellNum();

        for (int c = 0; c < columnNum; ++c) {
          XSSFCell cell1 = row1.getCell(c);
          XSSFCell cell2 = row2.getCell(c);
          XSSFCell cell3 = row3.getCell(c);

          if ((cell1 == null) || (cell2 == null) || (cell3 == null)) {
            logger.debug("读到一个空cell...");
            break;
          }

          cell1.setCellType(1);
          String value1 = cell1.getStringCellValue().trim();
          cell2.setCellType(1);
          String value2 = cell2.getStringCellValue().trim();
          cell3.setCellType(1);
          String value3 = cell3.getStringCellValue().trim();

          if (c == 0) {
            if (!("".equals(value1))) {
              String[] ss = splitName(value1);
              note = ss[0];
              name = ss[1];
              myClass.setNote(note);
              myClass.setName(name);

              list.add(myClass);
              logger.debug("类注释【{}】, 类名【{}】", new Object[] { note, name });
            }
          } else {
            if ("".equals(value1)) {
              logger.debug("读到一行的末尾...");
              break;
            }

            getPropertyWithLink(myClass, workbook, cell1, cell2, cell3);
          }

          logger.debug("读取到一个通信数据结构类【{}】", new Object[] { myClass.toString() });
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.error("出错之前读取的 ，sheetName【{}】, 类注释【{}】, 类名【{}】", new Object[] { sheetName, note, name });
    }

    return list;
  }

  private static void getPropertyWithLink(MyClass myClass, XSSFWorkbook workbook, XSSFCell cell1, XSSFCell cell2, XSSFCell cell3)
  {
    List propertyList = myClass.getPropertyList();
    Property property = ReadExcel.getPropertyByCell(cell1, cell2, cell3);
    propertyList.add(property);

    if (property.getType().contains("object")) {
      logger.debug("开始读取【{}】的链接", new Object[] { property.getNote() });
      XSSFHyperlink link = cell3.getHyperlink();
      if (link != null) {
        String address = link.getAddress();
        XSSFName xssfName = workbook.getName(address);
        String refers = xssfName.getRefersToFormula();

        XSSFCell cell = ExcelUtil.getCellByAddress(workbook, refers);

        cell.setCellType(1);
        String value = cell.getStringCellValue();
        String[] ss = splitName(value);

        String name = ss[1];

        property.setRealType(StringUtil.firstToUpperCase(name));
        property.setJavaType(StringUtil.firstToUpperCase(name));

        logger.debug("读取【{}】的链接结束", new Object[] { property.getNote() });
      }
    }
  }

  public static String[] splitName(String name)
  {
    String[] ss = name.split("\n");
    return ss;
  }
}