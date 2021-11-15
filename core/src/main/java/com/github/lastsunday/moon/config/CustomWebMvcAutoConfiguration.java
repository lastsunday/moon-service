package com.github.lastsunday.moon.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.github.lastsunday.moon.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.lastsunday.moon.config.app.Web;


@Configuration
public class CustomWebMvcAutoConfiguration implements WebMvcConfigurer {

	protected Logger log = LoggerFactory.getLogger(CustomWebMvcAutoConfiguration.class);

	@Autowired
	protected AppConfig appConfig;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// static resource
		Web web = appConfig.getWeb();
		//path = "" is root path
		addResourceHandlers(registry,web.getApp().isEnable(),"app");
		addResourceHandlers(registry,web.getMiniApp().isEnable(),"mini-app");
		addResourceHandlers(registry,web.getAdmin().isEnable(),"admin");
		addResourceHandlers(registry,web.getRepository().isEnable(),"repository");
	}

	private void addResourceHandlers(ResourceHandlerRegistry registry,boolean enable,String dir){
		addResourceHandlers(registry,enable,dir,"/"+dir);
	}

	private void addResourceHandlers(ResourceHandlerRegistry registry,boolean enable,String dir,String path){
		if (enable) {
			String mappingPath = path+"/**";
			log.info("Add ./{} folder resource handler mapping path = {}",dir,mappingPath);
			registry.addResourceHandler(mappingPath).addResourceLocations("file:./"+dir+"/");
		} else {
			log.info("Skip ./{} folder resource handler",dir);
		}
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		Web web = appConfig.getWeb();
		String rootRedirect = web.getRoot().getRedirect();
		if(StringUtils.isEmpty(rootRedirect) || "/".equals(rootRedirect)){
			log.info("Skip path redirect root");
		}else{
			//skip
			log.info("Add path {} redirect to {}","/",rootRedirect);
			registry.addRedirectViewController("/",rootRedirect);
		}
		addViewControllers(registry,web.getMiniApp().isEnable(),"app");
		addViewControllers(registry,web.getMiniApp().isEnable(),"mini-app");
		addViewControllers(registry,web.getAdmin().isEnable(),"admin");
		addViewControllers(registry,web.getRepository().isEnable(),"repository");
	}

	private void addViewControllers(ViewControllerRegistry registry,boolean enable,String dir){
		addViewControllers(registry,enable,dir,dir);
	}

	private void addViewControllers(ViewControllerRegistry registry,boolean enable,String dir,String path){
		String mappingPath = "/"+path;
		if (enable) {
			String redirectPath = null;
			if(dir.isEmpty()){
				redirectPath = "";
			}else{
				redirectPath = "/"+dir;
			}
			redirectPath+="/index.html";
			log.info("Add path {} redirect to {}",mappingPath,redirectPath);
			registry.addRedirectViewController(mappingPath,redirectPath);
		} else {
			log.info("Skip path {} redirect",mappingPath);
		}
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.stream().filter(converter -> converter instanceof StringHttpMessageConverter).forEach(
				converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
	}
}
