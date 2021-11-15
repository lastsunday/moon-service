package com.github.lastsunday.moon.controller;

import java.io.IOException;

import com.github.lastsunday.moon.controller.dto.CaptchaResultDTO;
import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "captcha", description = "captcha")
public interface CaptchaController extends AbstractController, DefaultExceptionConstant {

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	CaptchaResultDTO getCode() throws IOException;
}
