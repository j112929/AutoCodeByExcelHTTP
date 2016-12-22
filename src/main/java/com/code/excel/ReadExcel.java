/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.excel;

import com.code.model.MyClass;
import com.code.model.Property;
import com.code.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadExcel {
	private static final Logger logger = LoggerFactory.getLogger("ReadExcel");

	public static MyClass getMyClass(String fileName) {
		MyClass myClass = null;
		try {
			File file = new File(fileName);

			logger.info("########## 开始读Excel文件【{}】 ##########",
					new Object[] { fileName });

			InputStream is = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(is);

			int sheetNum = workbook.getNumberOfSheets();
			XSSFSheet sheet = null;
			for (int i = 0; i < sheetNum; ++i) {
				if ((!(workbook.isSheetHidden(i)))
						&& (!(workbook.isSheetVeryHidden(i)))) {
					sheet = workbook.getSheetAt(i);
					break;
				}

			}

			XSSFRow firstRow = sheet.getRow(0);
			XSSFCell firstCell = firstRow.getCell(0);
			firstCell.setCellType(1);
			String type = firstCell.getStringCellValue().trim();

			myClass = getMyClassBySheet(sheet);

			logger.info("########## 读Excel文件【{}】结束，文件对应的类信息【{}】 ##########",
					new Object[] { fileName, myClass.toString() });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return myClass;
	}

	private static MyClass getMyClassBySheet(XSSFSheet sheet) {
		MyClass myClass = new MyClass();
		List list = myClass.getPropertyList();

		int rowNum = sheet.getLastRowNum();

		XSSFRow firstRow = sheet.getRow(0);
		int columnNum = firstRow.getLastCellNum();
		logger.debug("sheetName【{}】,总行数【{}】,总列数【{}】",
				new Object[] { sheet.getSheetName(), Integer.valueOf(rowNum),
						Integer.valueOf(columnNum) });

		XSSFRow row1 = sheet.getRow(0);
		XSSFRow row2 = sheet.getRow(1);
		XSSFRow row3 = sheet.getRow(2);

		for (int j = 0; j < columnNum; ++j) {
			XSSFCell cell1 = row1.getCell(j);
			XSSFCell cell2 = row2.getCell(j);
			XSSFCell cell3 = row3.getCell(j);

			if ((cell1 == null) || (cell2 == null) || (cell3 == null)) {
				logger.debug(
						"从row中读到一个空Cell! sheetName【{}】,列【{}】",
						new Object[] { sheet.getSheetName(),
								Integer.valueOf(columnNum) });
			} else {
				Property property = getPropertyByCell(cell1, cell2, cell3);
				if ((property == null) || ("".equals(property.getType()))) {
					logger.debug(
							"从cell中读到一个空值! sheetName【{}】,列【{}】",
							new Object[] { sheet.getSheetName(),
									Integer.valueOf(columnNum) });
				} else {
					list.add(property);
				}
			}
		}

		return myClass;
	}

	private static MyClass getMyClassBySheet2(XSSFSheet sheet) {
		MyClass myClass = new MyClass();
		List list = myClass.getPropertyList();

		int rowNum = sheet.getLastRowNum();

		XSSFRow firstRow = sheet.getRow(1);
		int columnNum = firstRow.getLastCellNum();
		System.out.println("行数：" + rowNum + ", 列数：" + columnNum);

		for (int j = 4; j < rowNum; ++j) {
			XSSFRow row = sheet.getRow(j);
			XSSFCell cell1 = row.getCell(2);
			XSSFCell cell2 = row.getCell(0);
			XSSFCell cell3 = row.getCell(3);

			if ((cell1 == null) || (cell2 == null) || (cell3 == null)) {
				System.out.println("从row中读到一个空Cell...");
			} else {
				Property property = getPropertyByCell(cell1, cell2, cell3);
				if ((property == null) || ("".equals(property.getType()))) {
					System.out.println("从cell中读到一个空值...");
				} else {
					list.add(property);
				}
			}
		}
		return myClass;
	}

	public static Property getPropertyByCell(XSSFCell cell1, XSSFCell cell2,
			XSSFCell cell3) {
		cell1.setCellType(1);
		cell2.setCellType(1);
		cell3.setCellType(1);

		String type = cell1.getStringCellValue().trim();
		String name = cell2.getStringCellValue().trim();
		String note = cell3.getStringCellValue().trim();

		XSSFComment cellComment = cell3.getCellComment();
		if (cellComment != null) {
			String str = cellComment.getString().toString()
					.replaceAll("\n", "  ");
			str = str.replaceAll("：", ":");
			str = str.substring(Math.max(str.indexOf(":") + 1, 0));
			note = note + "  " + str;
		}

		if (("".equals(name)) || ("".equals(note)) || ("".equals(type))) {
			logger.debug("读到三个内容为空的单元格！列【{}】",
					new Object[] { Integer.valueOf(cell1.getColumnIndex()),
							Integer.valueOf(cell1.getRowIndex()) });
			return null;
		}

		Property property = new Property();
		property.setName(name);
		property.setNote(note);
		property.setType(type);
		property.setJavaType(StringUtil.getJavaType(type));
		property.setRealType(type);

		return property;
	}
}