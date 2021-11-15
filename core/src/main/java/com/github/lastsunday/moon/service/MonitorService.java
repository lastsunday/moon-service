package com.github.lastsunday.moon.service;

import com.github.lastsunday.moon.service.monitor.server.ServerInfo;

public interface MonitorService {

	ServerInfo fetchServerInfo();
}
