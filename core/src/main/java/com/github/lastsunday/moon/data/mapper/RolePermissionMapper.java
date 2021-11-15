package com.github.lastsunday.moon.data.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lastsunday.moon.data.domain.RolePermissionDO;

public interface RolePermissionMapper extends BaseMapper<RolePermissionDO> {

	default List<RolePermissionDO> selectRolePermissionByRoleId(String roleId) {
		RolePermissionDO rolePermissionDo = new RolePermissionDO();
		rolePermissionDo.setRoleId(roleId);
		QueryWrapper<RolePermissionDO> queryWrapper = new QueryWrapper<RolePermissionDO>(rolePermissionDo);
		return selectList(queryWrapper);
	}

	default int deleteRolePermissionByRoleId(String roleId) {
		RolePermissionDO rolePermissionDo = new RolePermissionDO();
		rolePermissionDo.setRoleId(roleId);
		QueryWrapper<RolePermissionDO> queryWrapper = new QueryWrapper<RolePermissionDO>(rolePermissionDo);
		return delete(queryWrapper);
	}
}
