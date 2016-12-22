/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model;

import com.code.model.abstractModel.AbstractField;
import com.code.util.StringUtil;

public class Property extends AbstractField {
	private String type;
	private String typeFU;
	private String realType;
	private String realTypeFU;
	private String javaType;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
		this.typeFU = StringUtil.firstToUpperCase(type);
	}

	public String getRealType() {
		return this.realType;
	}

	public void setRealType(String realType) {
		this.realType = realType;
		this.realTypeFU = StringUtil.firstToUpperCase(realType);
	}

	public String getTypeFU() {
		return this.typeFU;
	}

	public String getRealTypeFU() {
		return this.realTypeFU;
	}

	public String getJavaType() {
		return this.javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
}