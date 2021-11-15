package com.github.lastsunday.moon.data.mapper.user;

import java.util.HashSet;
import java.util.Set;

public class SelectUserDetailParam {
	private String account;
	private Set<String> roleIds = new HashSet<String>();

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Set<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<String> roleIds) {
		this.roleIds = roleIds;
	}
}
