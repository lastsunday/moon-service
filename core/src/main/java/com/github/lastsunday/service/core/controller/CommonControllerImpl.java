package com.github.lastsunday.service.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.lastsunday.service.core.util.CommonUtil;

@RestController
@RequestMapping("/api/common")
public class CommonControllerImpl implements CommonController {

	@Autowired
	protected CommonUtil commonUtil;

	@Override
	@RequestMapping(path = "version", method = RequestMethod.GET)
	public String version() {
		return commonUtil.getCurrentAppVersion();
	}

	@Override
	@RequestMapping(path = "datetime", method = RequestMethod.GET)
	public Date datetime() {
		return new Date();
	}

}
