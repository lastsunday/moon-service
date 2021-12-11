package com.github.lastsunday.moon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lastsunday.service.core.util.CommonUtil;


@Configuration
public class SwaggerConfig {

    protected Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Autowired
    protected AppConfig appConfig;
    @Autowired
    protected CommonUtil commonUtil;

    // see
    // https://springfox.github.io/springfox/docs/current/#springfox-spring-mvc-and-spring-boot
    @Bean
    public GroupedOpenApi publicApi() {
        log.info("Swagger enable = " + appConfig.getSwagger().isEnable());
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(commonUtil.getCurrentAppTitle())
                        .description(commonUtil.getCurrentAppTitle())
                        .version(commonUtil.getCurrentAppVersion())
                        .license(new License().name("Mulan Permissive Software License，Version 2").url("http://license.coscl.org.cn/MulanPSL2")))
                .externalDocs(new ExternalDocumentation()
                        .description("")
                        .url(""))
                .components(new Components().addSecuritySchemes("basicSchema", new SecurityScheme().name(appConfig.getService().getToken().getHeader()).type(SecurityScheme.Type.APIKEY).scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER)))
                .security(Arrays.asList(basicSecurityRequirement()));
    }

    private SecurityRequirement basicSecurityRequirement() {
        return new SecurityRequirement().addList("basicSchema");
    }

}
