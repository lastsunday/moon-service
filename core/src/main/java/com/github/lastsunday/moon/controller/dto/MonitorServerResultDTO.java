package com.github.lastsunday.moon.controller.dto;

import com.github.lastsunday.moon.service.monitor.server.ServerInfo;

public class MonitorServerResultDTO {

	private ServerInfo serverInfo;

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

}
