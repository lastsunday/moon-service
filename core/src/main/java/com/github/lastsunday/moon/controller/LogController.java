package com.github.lastsunday.moon.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.github.lastsunday.moon.controller.dto.FileLogGetParamDTO;
import com.github.lastsunday.moon.controller.dto.FileLogResultDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "log", description = "log")
public interface LogController extends AbstractController, LogExceptionConstant {

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
			+ MESSAGE_LOG_INVALID_ID + SWAGGER_API_NEWLINE + MESSAGE_LOG_FILE_NOT_EXISTS) })
	@PreAuthorize("@ss.hasPermission('system:log:list')")
	FileLogResultDTO list();

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	@PreAuthorize("@ss.hasPermission('system:log:get')")
	ResponseEntity<Resource> get(FileLogGetParamDTO param);

}
