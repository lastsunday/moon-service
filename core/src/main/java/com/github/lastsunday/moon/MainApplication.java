package com.github.lastsunday.moon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.lastsunday.moon.checker.AppConfigCheckerImpl;
import com.github.lastsunday.moon.starter.AppStarterImpl;
import com.github.lastsunday.service.core.checker.AppConfigChecker;
import com.github.lastsunday.service.core.starter.AppStarter;
import com.github.lastsunday.service.core.util.CommonUtil;

@SpringBootApplication(scanBasePackages = { "com.github.lastsunday.moon", "com.github.lastsunday.service.core" }, exclude = {
		RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class })
@EnableTransactionManagement
@EnableFeignClients
public class MainApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

	public static void main(String[] args) {
		run(args);
	}

	public static void run(String[] args) {
		new MainApplication().configure(new SpringApplicationBuilder(MainApplication.class)).run(args);
		log.info("System Ready");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.listeners(new ApplicationListener<ApplicationEvent>() {

			private Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

			@Override
			public void onApplicationEvent(ApplicationEvent event) {
				if (event instanceof ApplicationReadyEvent) {
					logger.info("onApplicationEvent ApplicationReadyEvent");
					ApplicationReadyEvent applicationReadyEvent = (ApplicationReadyEvent) event;
					ConfigurableApplicationContext configurableApplicationContext = applicationReadyEvent
							.getApplicationContext();
					CommonUtil commonUtil = configurableApplicationContext.getBean(CommonUtil.class);
					log.info("Current App = " + commonUtil.getCurrentAppTitle());
					log.info("Current Version = " + commonUtil.getCurrentAppVersion());
					AppConfigChecker appConfigChecker = configurableApplicationContext
							.getBean(AppConfigCheckerImpl.class);
					log.info("app config checking");
					appConfigChecker.check();
					AppStarter appStarter = configurableApplicationContext.getBean(AppStarterImpl.class);
					log.info("app start");
					appStarter.start();
				} else {
					// skip
				}
			}
		});
		return super.configure(builder);
	}
}
