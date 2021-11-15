package com.github.lastsunday.moon.controller.dto;

import java.util.Set;

public class UserListParamDTO extends AbstractPageParamDTO {

	private String account;
	private Set<String> roleIds;

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
