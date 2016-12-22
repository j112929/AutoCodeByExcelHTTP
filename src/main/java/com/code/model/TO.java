/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model;

public class TO extends MyClass {
	private Protocol request;
	private Protocol response;
	private Controller controller;

	public Protocol getRequest() {
		return this.request;
	}

	public void setRequest(Protocol request) {
		this.request = request;
	}

	public Protocol getResponse() {
		return this.response;
	}

	public void setResponse(Protocol response) {
		this.response = response;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}