package com.github.lastsunday.moon.controller;

import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

public interface UserExceptionConstant extends DefaultExceptionConstant {

	int ERROR_CODE_USER_DELETE_FAILURE_CANT_DELETE_SELF = 30000;
	int ERROR_CODE_USER_NOT_EXISTS = 30001;
	int ERROR_CODE_USER_CURRENT_PASSWORD_INCORRECT = 30002;
	int ERROR_CODE_USER_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD = 30003;

	String MESSAGE_USER_DELETE_FAILURE_CANT_DELETE_SELF = ERROR_CODE_USER_DELETE_FAILURE_CANT_DELETE_SELF
			+ ERROR_INFO_SEPARATOR + "Can't delete self.";

	String MESSAGE_USER_NOT_EXISTS = ERROR_CODE_USER_NOT_EXISTS + ERROR_INFO_SEPARATOR + "User not exists.";

}