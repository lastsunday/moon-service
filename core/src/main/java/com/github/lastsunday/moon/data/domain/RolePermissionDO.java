package com.github.lastsunday.moon.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.lastsunday.moon.data.domain.dto.RolePermissionDTO;

@TableName("role_permission")
public class RolePermissionDO extends DefaultDO<RolePermissionDTO> {

	@TableField("role_id")
	private String roleId;
	private String permission;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public RolePermissionDTO toData() {
		RolePermissionDTO result = new RolePermissionDTO();
		fillData(result);
		result.setPermission(getPermission());
		result.setRoleId(getRoleId());
		return result;
	}
}
