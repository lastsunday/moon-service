package com.github.lastsunday.moon.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.lastsunday.moon.service.log.FileLog;

@Service
public class LogServiceImpl implements LogService {

	@Value("${logging.file.name}")
	private String logFileName;

	@Override
	public List<FileLog> list() {
		List<FileLog> result = new ArrayList<FileLog>();
		File logDir = new File(new File(logFileName).getParent());
		File[] files = logDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			FileLog fileLog = new FileLog();
			fileLog.setId(file.getAbsolutePath().replace(logDir.getAbsolutePath(), ""));
			fileLog.setName(file.getName());
			fileLog.setPath(file.getAbsolutePath());
			fileLog.setLength(file.length());
			fileLog.setDirectory(file.isDirectory());
			fileLog.setModifyDate(new Date(file.lastModified()));
			try {
				fileLog.setCreateDate(new Date(
						((FileTime) Files.getAttribute(FileSystems.getDefault().getPath(file.getAbsolutePath()),
								"creationTime", LinkOption.NOFOLLOW_LINKS)).toMillis()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			result.add(fileLog);
		}
		return result;
	}

	@Override
	public String getLogFileAbsolutePathById(String id) {
		File logDir = new File(new File(logFileName).getParent());
		File targetFile = new File(logDir.getAbsolutePath() + id);
		if (targetFile.getAbsolutePath().startsWith(logDir.getAbsolutePath())) {
			if (targetFile.exists()) {
				return targetFile.getAbsolutePath();
			} else {
				throw new LogFileNotExistsException();
			}
		} else {
			throw new InvalidLogIdException();
		}
	}

}
