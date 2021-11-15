package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.ClientInfoResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginParamDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientResetPasswordParamDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "client", description = "client")
public interface ClientController extends AbstractController, ClientExceptionConstant {

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
			+ MESSAGE_CLIENT_VERIFY_CODE_NOT_SENT_OR_EXPIRED + SWAGGER_API_NEWLINE
			+ MESSAGE_CLIENT_VERIFY_CODE_NOT_CORRECT) })
	ClientLoginResultDTO login(ClientLoginParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	ClientInfoResultDTO info();

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
			+ MESSAGE_CLIENT_NOT_EXISTS + SWAGGER_API_NEWLINE + MESSAGE_CLIENT_CURRENT_PASSWORD_INCORRECT
			+ SWAGGER_API_NEWLINE + MESSAGE_CLIENT_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD) })
	void resetPassword(ClientResetPasswordParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	void logout();

}