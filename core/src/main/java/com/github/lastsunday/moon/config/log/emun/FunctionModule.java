package com.github.lastsunday.moon.config.log.emun;

/**
 * 功能模块
 */
public enum FunctionModule {
	//
	UNSPECIFIED(0),
	//
	USER(1),
	//
	ROLE(2),
	//
	CLIENT(3);

	private int code;

	private FunctionModule(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
