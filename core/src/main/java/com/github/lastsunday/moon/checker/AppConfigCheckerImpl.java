package com.github.lastsunday.moon.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.lastsunday.moon.config.AppConfig;
import com.github.lastsunday.service.core.checker.AppConfigChecker;

@Component
public class AppConfigCheckerImpl implements AppConfigChecker {

	public static final Logger log = LoggerFactory.getLogger(AppConfigCheckerImpl.class);

	@Autowired
	protected AppConfig appConfig;

	@Override
	public void check() {
		log.info("app.module.client.loginCaptchaCheckingEnable="
				+ appConfig.getModule().getClient().isLoginCaptchaCheckingEnable());
	}

}
