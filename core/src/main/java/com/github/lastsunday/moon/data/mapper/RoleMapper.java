package com.github.lastsunday.moon.data.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lastsunday.moon.data.domain.RoleDO;

public interface RoleMapper extends BaseMapper<RoleDO> {

	default Long selectCountByName(String name) {
		RoleDO param = new RoleDO();
		param.setName(name);
		QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<RoleDO>(param);
		return selectCount(queryWrapper);
	}

	default List<RoleDO> select(String name) {
		QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<RoleDO>();
		queryWrapper.like(name != null, "name", name);
		queryWrapper.orderByDesc("create_time", "update_time");
		return selectList(queryWrapper);
	}
}
