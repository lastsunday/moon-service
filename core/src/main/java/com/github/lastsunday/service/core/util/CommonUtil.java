package com.github.lastsunday.service.core.util;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

	public String getCurrentAppVersion() {
		String result = this.getClass().getPackage().getImplementationVersion();
		if (result != null && !result.isEmpty()) {
			return result;
		} else {
			return "";
		}
	}

	public String getCurrentAppTitle() {
		String result = this.getClass().getPackage().getImplementationTitle();
		if (result != null && !result.isEmpty()) {
			return result;
		} else {
			return "";
		}
	}
}
