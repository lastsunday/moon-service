package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.*;
import com.github.lastsunday.moon.data.domain.dto.RoleDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

@Tag(name = "role")
public interface RoleController extends AbstractController, RoleExceptionConstant {

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_ENTITY_NOT_EXISTS)})
    @PreAuthorize("@ss.hasPermission('system:role:get')")
    RoleDTO get(RoleGetParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:role:list')")
    PageResultDTO<RoleDTO> list(RoleListParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:role:create')")
    RoleCreateResultDTO create(RoleCreateParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:role:update')")
    void update(RoleUpdateParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + ERROR_CODE_ROLE_DELETE_FAILURE_CANT_DELETE_SELF)})
    @PreAuthorize("@ss.hasPermission('system:role:delete')")
    void delete(RoleDeleteParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    List<Map<String, Object>> options();

}
