package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.PermissionListAllResultDTO;
import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

@Tag(name = "user")
public interface PermissionController extends AbstractController, DefaultExceptionConstant {

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:permission:listAll')")
    PermissionListAllResultDTO listAll();

}
