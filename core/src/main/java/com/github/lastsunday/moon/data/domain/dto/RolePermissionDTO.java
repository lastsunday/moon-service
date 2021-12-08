package com.github.lastsunday.moon.data.domain.dto;

public class RolePermissionDTO extends DefaultDTO{
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
}
