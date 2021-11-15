package com.github.lastsunday.service.core.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alibaba.fastjson.JSON;
import com.github.lastsunday.service.core.CommonException;
import com.github.lastsunday.service.core.ErrorResponse;
import com.github.lastsunday.service.core.controller.DefaultExceptionConstant;

import feign.FeignException;
import feign.FeignException.BadRequest;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	public static final int INDEX_FIRST_FIELD_ERROR = 0;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		try {
			String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
			int splitIndex = message.indexOf(DefaultExceptionConstant.ERROR_INFO_SEPARATOR);
			String errorCodeString = message.substring(0, splitIndex);
			String errorMessageString = message.substring(splitIndex + 1, message.length());
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorCode(Integer.parseInt(errorCodeString));
			errorResponse.setErrorMessage(errorMessageString);
			return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ErrorResponse errorDetail = new ErrorResponse();
			FieldError fieldError = ex.getBindingResult().getFieldErrors().get(INDEX_FIRST_FIELD_ERROR);
			errorDetail.setErrorCode(DefaultExceptionConstant.ERROR_CODE_ARGUMENT_NOT_VALID);
			String fieldName = fieldError.getField();
			String defaultMessage = fieldError.getDefaultMessage();
			errorDetail.setErrorMessage(fieldName + " " + defaultMessage);
			return new ResponseEntity<Object>(errorDetail, HttpStatus.BAD_REQUEST);
		}
	}

	@ExceptionHandler(CommonException.class)
	public final ResponseEntity<Object> handleCommonException(CommonException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(ex.getErrorCode());
		errorResponse.setErrorMessage(ex.getErrorMessage());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FeignException.class)
	public final ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse();
		if (ex instanceof BadRequest) {
			BadRequest badRequest = (BadRequest) ex;
			String content = badRequest.contentUTF8();
			try {
				errorResponse = JSON.parseObject(content, ErrorResponse.class);
			} catch (Exception e) {
				errorResponse.setErrorCode(DefaultExceptionConstant.ERROR_CODE_REMOTE_SERVICE_EXCEPTION);
				errorResponse.setErrorMessage(e.getMessage());
			}
		} else {
			errorResponse.setErrorCode(DefaultExceptionConstant.ERROR_CODE_REMOTE_SERVICE_EXCEPTION);
			errorResponse.setErrorMessage(ex.getMessage());
		}
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}