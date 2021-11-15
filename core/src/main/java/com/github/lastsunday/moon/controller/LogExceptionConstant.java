package com.github.lastsunday.moon.controller;

import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

public interface LogExceptionConstant extends DefaultExceptionConstant {

	int ERROR_CODE_LOG_INVALID_ID = 50000;
	int ERROR_CODE_LOG_FILE_NOT_EXISTS = 50001;

	String MESSAGE_LOG_INVALID_ID = ERROR_CODE_LOG_INVALID_ID + ERROR_INFO_SEPARATOR + "invalid id.";

	String MESSAGE_LOG_FILE_NOT_EXISTS = ERROR_CODE_LOG_FILE_NOT_EXISTS + ERROR_INFO_SEPARATOR + "log file not exists.";
}
