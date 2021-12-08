package com.github.lastsunday.moon.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.lastsunday.moon.data.domain.dto.UserRoleDTO;

@TableName("user_role")
public class UserRoleDO extends DefaultDO<UserRoleDTO> {
	@TableField("user_id")
	private String userId;
	@TableField("role_id")
	private String roleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public UserRoleDTO toData() {
		UserRoleDTO result = new UserRoleDTO();
		fillData(result);
		result.setRoleId(getRoleId());
		result.setUserId(getUserId());
		return result;
	}
}
