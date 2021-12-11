package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.ClientInfoResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginParamDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientResetPasswordParamDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "client")
public interface ClientController extends AbstractController, ClientExceptionConstant {

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_CLIENT_VERIFY_CODE_NOT_SENT_OR_EXPIRED + SWAGGER_API_NEWLINE
            + MESSAGE_CLIENT_VERIFY_CODE_NOT_CORRECT)})
    @SecurityRequirements
    ClientLoginResultDTO login(ClientLoginParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    ClientInfoResultDTO info();

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_CLIENT_NOT_EXISTS + SWAGGER_API_NEWLINE + MESSAGE_CLIENT_CURRENT_PASSWORD_INCORRECT
            + SWAGGER_API_NEWLINE + MESSAGE_CLIENT_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD)})
    void resetPassword(ClientResetPasswordParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    void logout();

}