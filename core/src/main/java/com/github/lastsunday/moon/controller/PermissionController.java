package com.github.lastsunday.moon.controller;

import org.springframework.security.access.prepost.PreAuthorize;

import com.github.lastsunday.moon.controller.dto.PermissionListAllResultDTO;
import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "user", description = "user")
public interface PermissionController extends AbstractController, DefaultExceptionConstant {

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	@PreAuthorize("@ss.hasPermission('system:permission:listAll')")
	PermissionListAllResultDTO listAll();

}
