package com.github.lastsunday.moon.config.log.emun;

/**
 * 功能模块
 */
public enum FunctionModule {
	//
	UNSPECIFIED(0),
	//
	CLIENT(20000),
	//
	USER(30000),
	//
	ROLE(40000),
	//
	LOG(50000);

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
