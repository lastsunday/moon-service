package com.github.lastsunday.moon.data.enums;

public enum UserDeleteFlagType {

	ALIVE(0, "alive"), DELETED(1, "deleted");

	private int code;

	private String msg;

	private UserDeleteFlagType(int code, String msg) {
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
