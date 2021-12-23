package com.github.lastsunday.moon.controller;


import com.github.lastsunday.moon.controller.dto.*;
import com.github.lastsunday.moon.data.domain.dto.OperationLogDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@Tag(name = "log")
public interface LogController extends AbstractController, LogExceptionConstant {

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE + SWAGGER_API_NEWLINE
            + MESSAGE_LOG_INVALID_ID + SWAGGER_API_NEWLINE + MESSAGE_LOG_FILE_NOT_EXISTS)})
    @PreAuthorize("@ss.hasPermission('system:log:list')")
    FileLogResultDTO list();

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:log:get')")
    ResponseEntity<Resource> get(FileLogGetParamDTO param);

    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = DEFAULT_COMMON_EXCEPTION_MESSAGE)})
    @PreAuthorize("@ss.hasPermission('system:log:list')")
    PageResultDTO<OperationLogDTO> operationLogList(OperationLogListParamDTO param);
}
