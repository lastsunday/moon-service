package com.github.lastsunday.moon.controller;

import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

public interface RoleExceptionConstant extends DefaultExceptionConstant {

	int ERROR_CODE_ROLE_DELETE_FAILURE_CANT_DELETE_SELF = 40000;

	String MESSAGE_ROLE_DELETE_FAILURE_CANT_DELETE_SELF = ERROR_CODE_ROLE_DELETE_FAILURE_CANT_DELETE_SELF
			+ ERROR_INFO_SEPARATOR + "Can't delete self";
}