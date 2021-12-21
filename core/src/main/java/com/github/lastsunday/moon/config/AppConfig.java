package com.github.lastsunday.moon.config;

import com.github.lastsunday.moon.config.app.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class AppConfig {

	private Swagger swagger = new Swagger();
	private Service service = new Service();
	private Module module = new Module();
	private Remote remote = new Remote();
	private Web web = new Web();
	private DataKey dataKey = new DataKey();

	public Swagger getSwagger() {
		return swagger;
	}

	public void setSwagger(Swagger swagger) {
		this.swagger = swagger;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Remote getRemote() {
		return remote;
	}

	public void setRemote(Remote remote) {
		this.remote = remote;
	}

	public Web getWeb() {
		return web;
	}

	public void setWeb(Web web) {
		this.web = web;
	}

	public DataKey getDataKey() {
		return dataKey;
	}

	public void setDataKey(DataKey dataKey) {
		this.dataKey = dataKey;
	}
}