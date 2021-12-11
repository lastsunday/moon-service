package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.CaptchaResultDTO;
import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

@Tag(name = "captcha")
public interface CaptchaController extends AbstractController, DefaultExceptionConstant {

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @SecurityRequirements
    CaptchaResultDTO getCode() throws IOException;
}
