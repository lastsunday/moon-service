package com.github.lastsunday.moon.data.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lastsunday.moon.data.domain.UserRoleDO;

public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    default List<UserRoleDO> selectUserRoleByUserId(String userId) {
        UserRoleDO userRoleDo = new UserRoleDO();
        userRoleDo.setUserId(userId);
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<UserRoleDO>(userRoleDo);
        return selectList(queryWrapper);
    }

    default Long selectCountUserByRoleId(String roleId) {
        UserRoleDO userRoleDo = new UserRoleDO();
        userRoleDo.setRoleId(roleId);
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<UserRoleDO>(userRoleDo);
        return selectCount(queryWrapper);
    }

    default void deleteUserRoleByUserId(String userId) {
        UserRoleDO userRoleDo = new UserRoleDO();
        userRoleDo.setUserId(userId);
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<UserRoleDO>(userRoleDo);
        delete(queryWrapper);
    }
}
