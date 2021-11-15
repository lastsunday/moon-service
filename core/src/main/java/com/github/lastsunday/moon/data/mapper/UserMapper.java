package com.github.lastsunday.moon.data.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lastsunday.moon.data.domain.UserDO;
import com.github.lastsunday.moon.data.domain.dto.UserDTO;
import com.github.lastsunday.moon.data.mapper.user.SelectUserDetailParam;

public interface UserMapper extends BaseMapper<UserDO> {

	default UserDO select(String account) {
		UserDO userDo = new UserDO();
		userDo.setAccount(account);
		QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>(userDo);
		queryWrapper.orderByDesc("update_time", "create_time");
		return selectOne(queryWrapper);
	}

	List<UserDTO> selectUserDetail(SelectUserDetailParam param);

}
