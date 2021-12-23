package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.config.log.OperationLog;
import com.github.lastsunday.moon.config.log.emun.FunctionModule;
import com.github.lastsunday.moon.config.log.emun.Operation;
import com.github.lastsunday.moon.controller.dto.*;
import com.github.lastsunday.moon.data.DateRange;
import com.github.lastsunday.moon.data.domain.OperationLogDO;
import com.github.lastsunday.moon.data.domain.dto.OperationLogDTO;
import com.github.lastsunday.moon.data.mapper.OperationLogMapper;
import com.github.lastsunday.moon.service.InvalidLogIdException;
import com.github.lastsunday.moon.service.LogFileNotExistsException;
import com.github.lastsunday.moon.service.LogService;
import com.github.lastsunday.moon.util.StringUtils;
import com.github.lastsunday.service.core.CommonException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/system/log")
public class LogControllerImpl implements LogController {

    @Autowired
    private LogService logService;
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    @RequestMapping(path = "list", method = RequestMethod.POST)
    public FileLogResultDTO list() {
        FileLogResultDTO result = new FileLogResultDTO();
        result.setItems(copyList(logService.list(), FileLogItemDTO::new));
        return result;
    }

    @Override
    @RequestMapping(path = "get", method = RequestMethod.POST)
    @OperationLog(functionModule = FunctionModule.LOG, operation = Operation.LOG_GET)
    public ResponseEntity<Resource> get(@RequestBody @Valid FileLogGetParamDTO param) {
        try {
            String path = logService.getLogFileAbsolutePathById(param.getId());
            File file = new File(path);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok().headers(headers).contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        } catch (InvalidLogIdException e) {
            throw new CommonException(MESSAGE_LOG_INVALID_ID);
        } catch (LogFileNotExistsException e) {
            throw new CommonException(MESSAGE_LOG_FILE_NOT_EXISTS);
        } catch (FileNotFoundException e) {
            throw new CommonException(MESSAGE_LOG_FILE_NOT_EXISTS);
        }
    }

    @Override
    @RequestMapping(path = "operationLogList", method = RequestMethod.POST)
    public PageResultDTO<OperationLogDTO> operationLogList(@RequestBody @Valid OperationLogListParamDTO param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<OperationLogDO> list = operationLogMapper.selectList(
                StringUtils.isEmpty(param.getOperatorAccount()) ? null : param.getOperatorAccount(),
                StringUtils.isEmpty(param.getIp()) ? null : param.getIp(),
                new DateRange(param.getCreateTime()),
                param.getFunctionModule(), param.getOperation(),
                param.getErrorCode(),
                param.getNotHasErrorCode() == null ? false : param.getNotHasErrorCode(),
                StringUtils.isEmpty(param.getRequest()) ? null : param.getRequest(),
                StringUtils.isEmpty(param.getResponse()) ? null : param.getResponse(),
                param.getSortField() != null ? OperationLogMapper.SortField.valueOf(param.getSortField().name()) : null,
                param.getSortOrder());
        PageResultDTO<OperationLogDTO> result = copyAndReturn(list, OperationLogDTO::new);
        return result;
    }

}
