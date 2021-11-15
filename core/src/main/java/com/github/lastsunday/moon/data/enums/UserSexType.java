package com.github.lastsunday.moon.data.enums;

public enum UserSexType {

	FEMALE(0, "female"), MALE(1, "male"), UNKNOW(2, "unknow");

	private int code;

	private String msg;

	private UserSexType(int code, String msg) {
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
