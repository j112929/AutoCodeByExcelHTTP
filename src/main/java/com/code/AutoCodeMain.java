/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code;

import com.code.config.CoderConfig;
import com.code.config.Config;
import com.code.excel.ReadProtocolExcel;
import com.code.freemarker.FreeMarkerWriter;
import com.code.log.LogStart;
import com.code.model.Controller;
import com.code.model.Module;
import com.code.model.MyClass;
import com.code.model.Protocol;
import com.code.model.TO;
import com.code.util.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoCodeMain
{
  private static final Logger logger = LoggerFactory.getLogger("AutoCodeMain");
  public static final String EXCEL_FILE_SUFFIX = ".xlsx";

  public static void main(String[] args)
    throws Exception
  {
    new LogStart();
    logger.info("################## 开始生成代码 #################");

    codeProtocol();

    logger.info("################## 代码生成完毕 ##################");
    Thread.sleep(3000L);
  }

  public static void codeProtocol()
  {
    List publicTOList = ReadProtocolExcel.readStructExcel(Config.protocolExcelName, "数据结构");

    Map moduleMap = ReadProtocolExcel.readProtocolExcel(Config.protocolExcelName, "请求响应");

    List requestTOList = moduleMap2ProtocolList(moduleMap, "Request");

    List responseTOList = moduleMap2ProtocolList(moduleMap, "Response");

    List controllerList = moduleMap2ControllerList(moduleMap);

    for (CoderConfig config : Config.coderConfigList)
    {
      String type = config.getType();
      String method = config.getMethod();
      String tempFile = config.getTempFile();
      Boolean isWriteToProject = Boolean.valueOf(config.isWriteToProject());
      Boolean isForceWriteToProject = Boolean.valueOf(config.isForceWriteToProject());
      String outFilePath = config.getOutFilePath();

      if (method.equalsIgnoreCase("codePublicTO"))
        codePublicTO(publicTOList, type, tempFile, isWriteToProject.booleanValue(), isForceWriteToProject.booleanValue(), outFilePath);
      else if (method.equalsIgnoreCase("codeRequestTO"))
        codeRequestTO(requestTOList, type, tempFile, isWriteToProject.booleanValue(), isForceWriteToProject.booleanValue(), outFilePath);
      else if (method.equalsIgnoreCase("codeResponseTO"))
        codeRequestTO(responseTOList, type, tempFile, isWriteToProject.booleanValue(), isForceWriteToProject.booleanValue(), outFilePath);
      else if (method.equalsIgnoreCase("codeController"))
        codeController(controllerList, type, tempFile, isWriteToProject.booleanValue(), isForceWriteToProject.booleanValue(), outFilePath);
      else if (method.equalsIgnoreCase("codeControllerList"))
        codeControllerList(controllerList, type, tempFile, isWriteToProject.booleanValue(), isForceWriteToProject.booleanValue(), outFilePath);
      else if (method.equalsIgnoreCase("copyFiles")) {
        codeFiles(type, tempFile, isWriteToProject.booleanValue(), outFilePath);
      }
      else if (!(type.toUpperCase().contains("SERVER")))
      {
        if (!(type.toUpperCase().contains("CLIENT")))
        {
          throw new RuntimeException("错误的type【" + type + "】，type 必须是 server 或者 client 请检查配置！");
        }
      }
    }
  }

  public static void codePublicTO(List<MyClass> structList, String type, String tempFile, boolean isWriteToProject, boolean isForceWriteToProject, String outFilePath)
  {
    for (MyClass myClass : structList) {
      if (isWriteToProject)
      {
        String modelName = outFilePath.replaceAll("\\{\\}", myClass.getNameFU());
        FreeMarkerWriter.write(tempFile, modelName, myClass, isForceWriteToProject);
      }

      String fileName = outFilePath.substring(outFilePath.lastIndexOf("\\"));
      String modelName2 = Config.outputPath + type + "\\" + "apito\\" + fileName.replaceAll("\\{\\}", myClass.getNameFU());
      FreeMarkerWriter.write(tempFile, modelName2, myClass, true);
    }
  }

  public static void codeRequestTO(List<Protocol> list, String type, String tempFile, boolean isWriteToProject, boolean isForceWriteToProject, String outFilePath)
  {
    for (Protocol pClass : list) {
      String packageName = pClass.getPackageName();
      if (isWriteToProject)
      {
        String modelName = outFilePath.replaceAll("\\{package\\}", packageName).replaceAll("\\{\\}", pClass.getNameFU());
        FreeMarkerWriter.write(tempFile, modelName, pClass, isForceWriteToProject);
      }

      String fileName = outFilePath.substring(outFilePath.lastIndexOf("\\") + 1);
      String modelName2 = Config.outputPath + type + "\\" + "apito\\" + packageName + "\\" + fileName.replaceAll("\\{\\}", pClass.getNameFU());
      FreeMarkerWriter.write(tempFile, modelName2, pClass, true);
    }
  }

  public static void codeController(List<Controller> controllerList, String type, String tempFile, boolean isWriteToProject, boolean isForceWriteToProject, String outFilePath)
  {
    for (Controller controller : controllerList) {
      String packageName = controller.getPackageName();
      if (isWriteToProject)
      {
        String modelName = outFilePath.replaceAll("\\{package\\}", packageName).replaceAll("\\{\\}", controller.getNameFU());
        FreeMarkerWriter.write(tempFile, modelName, controller, isForceWriteToProject);
      }

      String fileName = outFilePath.substring(outFilePath.lastIndexOf("\\") + 1);
      String modelName2 = Config.outputPath + type + "\\" + "controller\\" + packageName + "\\" + fileName.replaceAll("\\{\\}", controller.getNameFU());
      FreeMarkerWriter.write(tempFile, modelName2, controller, true);
    }
  }

  public static void codeFiles(String type, String tempFile, boolean isWriteToProject, String outFilePath)
  {
    tempFile = FreeMarkerWriter.filePath + tempFile;

    if (isWriteToProject)
    {
      FileUtil.copyDirectory(tempFile, outFilePath);
    }

    String outFilePath2 = Config.outputPath + type + "\\";
    FileUtil.copyDirectory(tempFile, outFilePath2);
  }

  public static void codeControllerList(List<Controller> controllerList, String type, String tempFile, boolean isWriteToProject, boolean isForceWriteToProject, String outFilePath)
  {
    Map map = new HashMap();
    map.put("controllerList", controllerList);

    if (isWriteToProject)
    {
      String modelName = outFilePath;
      FreeMarkerWriter.write(tempFile, modelName, map, isForceWriteToProject);
    }

    String fileName = outFilePath.substring(outFilePath.lastIndexOf("\\") + 1);
    String modelName2 = Config.outputPath + type + "\\" + fileName;
    FreeMarkerWriter.write(tempFile, modelName2, map, true);
  }

  public static void codeMsgFactory(Map<String, Module> responseModuleMap, String type, String tempFile, boolean isWriteToProject, boolean isForceWriteToProject, String outFilePath)
  {
    for (Module module : responseModuleMap.values())
    {
      if (isWriteToProject)
      {
        String modelName = outFilePath.replaceAll("\\{\\}", module.getNameFU());
        FreeMarkerWriter.write(tempFile, modelName, module, isForceWriteToProject);
      }

      String fileName = outFilePath.substring(outFilePath.lastIndexOf("\\") + 1);
      String modelName2 = Config.outputPath + type + "\\" + "msgFactory\\" + fileName.replaceAll("\\{\\}", module.getNameFU());
      FreeMarkerWriter.write(tempFile, modelName2, module);
    }
  }

  public static void codeStructFactory(List<MyClass> structList, String type, String tempFile, boolean isWriteToProject, boolean isForceWriteToProject, String outFilePath)
  {
    for (MyClass pClass : structList) {
      if (isWriteToProject)
      {
        String modelName = outFilePath.replaceAll("\\{\\}", pClass.getNameFU());
        FreeMarkerWriter.write(tempFile, modelName, pClass, isForceWriteToProject);
      }

      String fileName = outFilePath.substring(outFilePath.lastIndexOf("\\") + 1);
      String modelName2 = Config.outputPath + type + "\\" + "structFactory\\" + fileName.replaceAll("\\{\\}", pClass.getNameFU());
      FreeMarkerWriter.write(tempFile, modelName2, pClass);
    }
  }

  public static List<Protocol> moduleMap2ProtocolList(Map<String, Module> moduleMap, String type)
  {
    List list = new ArrayList();

    for (Module module : moduleMap.values())
    {
      List<Controller> controllerList = module.getControllerList();
      for (Controller c : controllerList) {
        List<TO> toList = c.getToList();
        for (TO to : toList) {
          Protocol protocol = null;
          if (type == "Request")
            protocol = to.getRequest();
          else if (type == "Response") {
            protocol = to.getResponse();
          }
          list.add(protocol);
        }
      }
    }

    return list;
  }

  public static List<Controller> moduleMap2ControllerList(Map<String, Module> moduleMap)
  {
    List list = new ArrayList();

    for (Module module : moduleMap.values())
    {
      List controllerList = module.getControllerList();
      list.addAll(controllerList);
    }

    return list;
  }
}