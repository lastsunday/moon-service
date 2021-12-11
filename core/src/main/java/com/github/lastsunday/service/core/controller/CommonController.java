package com.github.lastsunday.service.core.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Date;


@Tag(name = "common")
public interface CommonController {

    @SecurityRequirements
    String version();

    @SecurityRequirements
    Date datetime();
}
