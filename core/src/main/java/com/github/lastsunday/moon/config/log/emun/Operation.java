package com.github.lastsunday.moon.config.log.emun;

public enum Operation {

	//
	UNSPECIFIED(0),
	//
	ADD(1),
	//
	DELETE(2),
	//
	UPDATE(3),
	//
	LOGIN(4);

	private int code;

	private Operation(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
