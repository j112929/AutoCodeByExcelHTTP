/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.config;

import com.code.util.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Config
{
  private static String configPath = System.getProperty("file.separator") + System.getProperty("user.dir") + System.getProperty("file.separator") + "conf" + System.getProperty("file.separator");

  public static String outputPath = System.getProperty("file.separator") + System.getProperty("user.dir") + System.getProperty("file.separator") + "code" + System.getProperty("file.separator");

  public static String time = StringUtil.getFormatTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
  public static String timeDay = StringUtil.getFormatTime(System.currentTimeMillis(), "yyyy-MM-dd");
  public static String timeMonth = StringUtil.getFormatTime(System.currentTimeMillis(), "yyyy-MM");
  public static String timeYear = StringUtil.getFormatTime(System.currentTimeMillis(), "yyyy");
  public static String author = "AutoCode";
  public static String email = "1129290218@qq.com";
  public static String encoding;
  public static String protocolExcelName;
  public static List<CoderConfig> coderConfigList = new ArrayList<CoderConfig>();

  public static void initConfig(String fileName)
  {
    Document doc = null;
    try {
      SAXBuilder sb = new SAXBuilder();
      File file = new File(fileName);
      doc = sb.build(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Element root = doc.getRootElement();
    List<Element> coders = root.getChildren("coder");
    List<Element> names = root.getChildren("name");
    List<Element> dbs = root.getChildren("db");

    encoding = root.getAttributeValue("encoding").trim();
    protocolExcelName = root.getAttributeValue("protocolExcelName").trim();

    for (Element e : coders) {
      String type = e.getAttributeValue("type").trim();
      String method = e.getAttributeValue("method").trim();
      String tempFile = e.getAttributeValue("tempFile").trim();
      Boolean isWriteToProject = Boolean.valueOf(e.getAttributeValue("isWriteToProject").trim());
      Boolean isForceWriteToProject = Boolean.valueOf(e.getAttributeValue("isForceWriteToProject").trim());
      String outFilePath = e.getAttributeValue("outFilePath").trim();
      CoderConfig coderConfig = new CoderConfig();
      coderConfig.setType(type);
      coderConfig.setMethod(method);
      coderConfig.setTempFile(tempFile);
      coderConfig.setWriteToProject(isWriteToProject.booleanValue());
      coderConfig.setForceWriteToProject(isForceWriteToProject.booleanValue());
      coderConfig.setOutFilePath(outFilePath);
      coderConfigList.add(coderConfig);
    }
  }

  static
  {
    initConfig(configPath + "config.xml");
  }
}