package com.github.lastsunday.moon.data.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;

public class UserRoleDTO extends DefaultDTO{

    private String userId;
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
}
