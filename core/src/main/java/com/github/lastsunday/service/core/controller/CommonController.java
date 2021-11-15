package com.github.lastsunday.service.core.controller;

import java.util.Date;

import io.swagger.annotations.Api;

@Api(tags = "common", description = "common")
public interface CommonController {

	String version();

	Date datetime();
}
