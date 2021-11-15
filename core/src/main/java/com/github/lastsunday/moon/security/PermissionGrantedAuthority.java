package com.github.lastsunday.moon.security;

import org.springframework.security.core.GrantedAuthority;

public class PermissionGrantedAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5672324670161358211L;

	private String permission;

	public PermissionGrantedAuthority(String permission) {
		super();
		this.permission = permission;
	}

	@Override
	public String getAuthority() {
		return permission;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}