package com.github.lastsunday.moon.config.app;

import com.github.lastsunday.moon.config.app.module.Client;

public class Module {

	private Client client = new Client();

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
