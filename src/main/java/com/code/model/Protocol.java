/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model;

public class Protocol extends MyClass {
	public static final String REQUEST = "Request";
	public static final String RESPONSE = "Response";
	public static final String STRUCT = "Struct";
	public String protocolType;
	private Controller controller;

	public String getProtocolType() {
		return this.protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}