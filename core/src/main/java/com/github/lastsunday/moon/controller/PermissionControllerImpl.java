package com.github.lastsunday.moon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.lastsunday.moon.controller.dto.PermissionListAllResultDTO;
import com.github.lastsunday.service.core.security.PermissionManager;

@RestController
@RequestMapping("/api/system/permission")
public class PermissionControllerImpl implements PermissionController {

	@Autowired
	protected PermissionManager permissionManager;

	@Override
	@RequestMapping(path = "listAll", method = RequestMethod.POST)
	public PermissionListAllResultDTO listAll() {
		PermissionListAllResultDTO result = new PermissionListAllResultDTO();
		result.setItems(permissionManager.getAllPermissions());
		return result;
	}

}
