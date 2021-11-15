package com.github.lastsunday.moon.data.enums;

public enum UserTypeType {

	SYSTEM(0, "system");

	private int code;

	private String msg;

	private UserTypeType(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
