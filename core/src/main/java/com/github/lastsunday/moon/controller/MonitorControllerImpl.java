package com.github.lastsunday.moon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.lastsunday.moon.controller.dto.MonitorServerResultDTO;
import com.github.lastsunday.moon.service.MonitorService;

@RestController
@RequestMapping("/api/system/monitor")
public class MonitorControllerImpl implements MonitorController {

	@Autowired
	private MonitorService monitorService;

	@Override
	@RequestMapping(path = "server", method = RequestMethod.POST)
	public MonitorServerResultDTO server() {
		MonitorServerResultDTO result = new MonitorServerResultDTO();
		result.setServerInfo(monitorService.fetchServerInfo());
		return result;
	}

}
