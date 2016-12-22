/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model.abstractModel;

import com.code.config.Config;
import com.code.util.StringUtil;

public abstract class AbstractField extends AbstractObject {
	public String time;
	public String timeDay;
	public String timeMonth;
	public String timeYear;
	public String name;
	public String note;
	public String nameFU;
	public String nameAU;
	public String email;
	public String author;

	public AbstractField() {
		this.time = StringUtil.getFormatTime(System.currentTimeMillis(),
				"yyyy-MM-dd HH:mm:ss");
		this.timeDay = StringUtil.getFormatTime(System.currentTimeMillis(),
				"yyyy-MM-dd");
		this.timeMonth = StringUtil.getFormatTime(System.currentTimeMillis(),
				"yyyy-MM");
		this.timeYear = StringUtil.getFormatTime(System.currentTimeMillis(),
				"yyyy");
		this.email = Config.email;
		this.author = Config.author;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		this.nameFU = StringUtil.firstToUpperCase(name);
		this.nameAU = name.toUpperCase();
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNameFU() {
		return this.nameFU;
	}

	public void setNameFU(String nameFU) {
		this.nameFU = nameFU;
	}

	public String getNameAU() {
		return this.nameAU;
	}

	public void setNameAU(String nameAU) {
		this.nameAU = nameAU;
	}

	public String getTime() {
		return this.time;
	}

	public String getTimeDay() {
		return this.timeDay;
	}

	public String getTimeMonth() {
		return this.timeMonth;
	}

	public String getTimeYear() {
		return this.timeYear;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}