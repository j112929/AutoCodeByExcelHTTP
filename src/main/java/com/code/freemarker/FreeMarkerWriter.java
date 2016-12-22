/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.freemarker;

import com.code.config.Config;
import com.code.util.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreeMarkerWriter {
	private static final Logger logger = LoggerFactory
			.getLogger("FreeMarkerWriter");

	public static String filePath = System.getProperty("file.separator")
			+ System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "freeMarkerTemp"
			+ System.getProperty("file.separator");

	public static void write(String templateFileName, String outFileFullName,
			Object data) {
		write(templateFileName, outFileFullName, data, true);
	}

	public static void write(String templateFileName, String outFileFullName,
			Object data, boolean isCoverIfExists) {
		boolean isValid = checkValid(templateFileName);

		if (!(isValid)) {
			throw new RuntimeException(filePath + templateFileName
					+ " is a Invalid FreeMarker template");
		}

		if (data instanceof Map) {
			Map map = (Map) data;
			map.put("author", Config.author);
			map.put("email", Config.email);
			map.put("time", Config.time);
			map.put("timeDay", Config.timeDay);
			map.put("timeMonth", Config.timeMonth);
			map.put("timeYear", Config.timeYear);
		}

		File outFile = new File(outFileFullName);

		if (outFile.exists()) {
			if (!(isCoverIfExists)) {
				logger.debug("文件【" + outFileFullName + "】已经存在！，不覆盖！");
				return;
			}
			if (isCoverIfExists)
				logger.debug("文件【" + outFileFullName + "】已经存在！，覆盖旧的文件！！！");
		} else {
			outFile = FileUtil.makeDirAndFile(outFileFullName);
			logger.debug("文件【" + outFileFullName + "】不存在！，创建新文件！");
		}

		Configuration cfg = new Configuration();
		FileOutputStream fos = null;
		try {
			cfg.setDirectoryForTemplateLoading(new File(filePath));

			Template t = cfg.getTemplate(templateFileName, "utf-8");
			fos = new FileOutputStream(new File(outFileFullName));

			t.process(data, new OutputStreamWriter(fos, "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean checkValid(String templateFileName) {
		File file = new File(filePath + templateFileName);
		boolean hasEmail = false;
		boolean hasAuthor = false;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if (line.contains("${email}")) {
					hasEmail = true;
				}
				if (line.contains("${author}")) {
					hasAuthor = true;
				}
				if ((hasEmail) && (hasAuthor)) {
					return true;
				}
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ((hasEmail) && (hasAuthor));
	}
}