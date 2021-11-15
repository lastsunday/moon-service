package com.github.lastsunday.service.core;

import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

public class CommonException extends RuntimeException {

	public static final int UNSPECIFIED_EXCEPTION = -1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3735736909063836034L;

	private int errorCode;
	private String errorMessage;

	public CommonException(String errorCodeAndErrorMessage) {
		super();
		int splitIndex = errorCodeAndErrorMessage.indexOf(DefaultExceptionConstant.ERROR_INFO_SEPARATOR);
		String errorCodeString = errorCodeAndErrorMessage.substring(0, splitIndex);
		String errorMessageString = errorCodeAndErrorMessage.substring(splitIndex + 1,
				errorCodeAndErrorMessage.length());
		this.errorCode = Integer.parseInt(errorCodeString);
		this.errorMessage = errorMessageString;
	}

	public CommonException(int errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
