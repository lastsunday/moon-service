package com.github.lastsunday.moon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lastsunday.service.core.util.CommonUtil;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

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
	public Docket petApi() {
		log.info("Swagger enable = " + appConfig.getSwagger().isEnable());
		return new Docket(DocumentationType.OAS_30).enable(appConfig.getSwagger().isEnable()).apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.any()).paths(new Predicate<String>() {

					@Override
					public boolean test(String t) {
						return !t.matches("/error.*");
					}
				}).paths(PathSelectors.any()).build().pathMapping("/").securitySchemes(Arrays.asList(apiKey()))
				.securityContexts(Arrays.asList(securityContext())).enableUrlTemplating(true);
	}

	@SuppressWarnings("rawtypes")
	private ApiInfo apiInfo() {
		return new ApiInfo(commonUtil.getCurrentAppTitle(), commonUtil.getCurrentAppTitle(),
				commonUtil.getCurrentAppVersion(), "", new Contact("", "", ""), "", "",
				new ArrayList<VendorExtension>());
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.operationSelector(new Predicate<OperationContext>() {

					@Override
					public boolean test(OperationContext t) {
						return t.requestMappingPattern().matches("^/api/(?!client/login)(?!common/).*$");
					}
				}).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}
}
