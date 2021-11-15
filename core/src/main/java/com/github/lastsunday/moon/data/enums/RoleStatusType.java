package com.github.lastsunday.moon.data.enums;

public enum RoleStatusType {

	NORMAL(0, "enabled"), DISABLE(1, "disabled");

	private int code;

	private String msg;

	private RoleStatusType(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static RoleStatusType getByCode(Integer code) {
		RoleStatusType[] values = values();
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
