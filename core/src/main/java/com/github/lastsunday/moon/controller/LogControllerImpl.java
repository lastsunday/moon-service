package com.github.lastsunday.moon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.validation.Valid;

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

import com.github.lastsunday.moon.controller.dto.FileLogGetParamDTO;
import com.github.lastsunday.moon.controller.dto.FileLogItemDTO;
import com.github.lastsunday.moon.controller.dto.FileLogResultDTO;
import com.github.lastsunday.moon.service.InvalidLogIdException;
import com.github.lastsunday.moon.service.LogFileNotExistsException;
import com.github.lastsunday.moon.service.LogService;
import com.github.lastsunday.service.core.CommonException;

@RestController
@RequestMapping("/api/system/log")
public class LogControllerImpl implements LogController {

	@Autowired
	private LogService logService;

	@Override
	@RequestMapping(path = "list", method = RequestMethod.POST)
	public FileLogResultDTO list() {
		FileLogResultDTO result = new FileLogResultDTO();
		result.setItems(copyList(logService.list(), FileLogItemDTO::new));
		return result;
	}

	@Override
	@RequestMapping(path = "get", method = RequestMethod.POST)
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

}
