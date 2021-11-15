package com.github.lastsunday.service.core;

public class ErrorResponse {

	private int errorCode;
	private String errorMessage;
	private Object errorDetail;

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

	public Object getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(Object errorDetail) {
		this.errorDetail = errorDetail;
	}

}