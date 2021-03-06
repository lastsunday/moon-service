package com.github.lastsunday.moon.controller;

import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

public interface ClientExceptionConstant extends DefaultExceptionConstant {
	int ERROR_CODE_CLIENT_NAME_OR_PASSWORD_NOT_MATCH = 20000;
	int ERROR_CODE_CLIENT_VERIFY_CODE_NOT_SENT_OR_EXPIRED = 20001;
	int ERROR_CODE_CLIENT_VERIFY_CODE_NOT_CORRECT = 20002;
	int ERROR_CODE_CLIENT_NOT_EXISTS = 20003;
	int ERROR_CODE_CLIENT_CURRENT_PASSWORD_INCORRECT = 20004;
	int ERROR_CODE_CLIENT_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD = 20005;

	String MESSAGE_CLIENT_NAME_OR_PASSWORD_NOT_MATCH = ERROR_CODE_CLIENT_NAME_OR_PASSWORD_NOT_MATCH
			+ ERROR_INFO_SEPARATOR + "user name or passowrd not match";

	String MESSAGE_CLIENT_VERIFY_CODE_NOT_SENT_OR_EXPIRED = ERROR_CODE_CLIENT_VERIFY_CODE_NOT_SENT_OR_EXPIRED
			+ ERROR_INFO_SEPARATOR + "verify code not sent or expired";

	String MESSAGE_CLIENT_VERIFY_CODE_NOT_CORRECT = ERROR_CODE_CLIENT_VERIFY_CODE_NOT_CORRECT + ERROR_INFO_SEPARATOR
			+ "verify code not correct";

	String MESSAGE_CLIENT_NOT_EXISTS = ERROR_CODE_CLIENT_NOT_EXISTS + ERROR_INFO_SEPARATOR + "Client not exists.";

	String MESSAGE_CLIENT_CURRENT_PASSWORD_INCORRECT = ERROR_CODE_CLIENT_CURRENT_PASSWORD_INCORRECT
			+ ERROR_INFO_SEPARATOR + "Current password incorrect.";

	String MESSAGE_CLIENT_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD = ERROR_CODE_CLIENT_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD
			+ ERROR_INFO_SEPARATOR + "New password can not equal current password.";
}
