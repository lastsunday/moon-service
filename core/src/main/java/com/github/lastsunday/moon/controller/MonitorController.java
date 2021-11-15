package com.github.lastsunday.moon.controller;

import org.springframework.security.access.prepost.PreAuthorize;

import com.github.lastsunday.moon.controller.dto.MonitorServerResultDTO;
import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "monitor", description = "monitor")
public interface MonitorController extends AbstractController, DefaultExceptionConstant {

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	@PreAuthorize("@ss.hasPermission('system:monitor:server')")
	MonitorServerResultDTO server();
}
