package com.github.lastsunday.moon.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.lastsunday.service.core.security.PermissionManager;
import com.github.lastsunday.service.core.starter.AppStarter;

@Component
public class AppStarterImpl implements AppStarter {

	@Autowired
	protected PermissionManager permissionManager;

	@Override
	public void start() {
		permissionManager.fetchSystemPermission();
	}

}
