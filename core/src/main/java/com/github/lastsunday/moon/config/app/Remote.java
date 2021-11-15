package com.github.lastsunday.moon.config.app;

import com.github.lastsunday.moon.config.app.remote.Service;

public class Remote {

	private Service service = new Service();

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
