package com.github.lastsunday.moon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import com.github.lastsunday.moon.controller.dto.PageResultDTO;
import com.github.lastsunday.moon.controller.dto.RoleCreateParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleCreateResultDTO;
import com.github.lastsunday.moon.controller.dto.RoleDeleteParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleGetParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleListParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleUpdateParamDTO;
import com.github.lastsunday.moon.data.domain.dto.RoleDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "role", description = "role")
public interface RoleController extends AbstractController, RoleExceptionConstant {

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
			+ MESSAGE_ENTITY_NOT_EXISTS) })
	@PreAuthorize("@ss.hasPermission('system:role:get')")
	RoleDTO get(RoleGetParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	@PreAuthorize("@ss.hasPermission('system:role:list')")
	PageResultDTO<RoleDTO> list(RoleListParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	@PreAuthorize("@ss.hasPermission('system:role:create')")
	RoleCreateResultDTO create(RoleCreateParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	@PreAuthorize("@ss.hasPermission('system:role:update')")
	void update(RoleUpdateParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
			+ ERROR_CODE_ROLE_DELETE_FAILURE_CANT_DELETE_SELF) })
	@PreAuthorize("@ss.hasPermission('system:role:delete')")
	void delete(RoleDeleteParamDTO param);

	@ApiResponses(value = { @ApiResponse(code = 400, message = DEFAULT_COMMON_EXCEPTION_MESSAGE) })
	List<Map<String, Object>> options();

}
