package com.github.lastsunday.moon.service;

import java.util.List;

import com.github.lastsunday.moon.service.log.FileLog;

public interface LogService {

	List<FileLog> list();
	
	String getLogFileAbsolutePathById(String id);
}
