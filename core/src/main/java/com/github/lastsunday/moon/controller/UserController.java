package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.*;
import com.github.lastsunday.moon.data.domain.dto.UserDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;

@Tag(name = "user")
public interface UserController extends AbstractController, UserExceptionConstant {

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_ENTITY_NOT_EXISTS)})
    @PreAuthorize("@ss.hasPermission('system:user:get')")
    UserDTO get(UserGetParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:user:list')")
    PageResultDTO<UserDTO> list(UserListParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_ENTITY_EXISTS)})
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    UserCreateResultDTO create(@Valid UserCreateParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_ENTITY_UPDATE_FAILURE)})
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    void update(UserUpdateParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_ENTITY_DELETE_FAILURE + SWAGGER_API_NEWLINE + MESSAGE_USER_DELETE_FAILURE_CANT_DELETE_SELF)})
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    void delete(UserDeleteParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_USER_NOT_EXISTS)})
    @PreAuthorize("@ss.hasPermission('system:user:resetPassword')")
    void resetPassword(UserResetPasswordParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:user:online:list')")
    PageResultDTO<UserOnlineDTO> list(UserOnlineListParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:user:online:forceLogout')")
    void forceLogout(UserOnlineForceLogoutParamDTO param);

}
