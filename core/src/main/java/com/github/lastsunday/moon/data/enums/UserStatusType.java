package com.github.lastsunday.moon.data.enums;

public enum UserStatusType {

	NORMAL(0, "enabled"), DISABLE(1, "disabled");

	private int code;

	private String msg;

	private UserStatusType(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static UserStatusType getByCode(Integer code) {
		UserStatusType[] values = values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].getCode() == code) {
				return values[i];
			} else {
				// skip
			}
		}
		throw new RuntimeException("unknow status code = " + code);
	}
}
