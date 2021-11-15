package com.github.lastsunday.moon.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.lastsunday.moon.data.domain.RolePermissionDO;
import com.github.lastsunday.moon.data.domain.UserDO;
import com.github.lastsunday.moon.data.domain.UserRoleDO;
import com.github.lastsunday.moon.data.enums.UserStatusType;
import com.github.lastsunday.moon.data.mapper.RolePermissionMapper;
import com.github.lastsunday.moon.data.mapper.UserMapper;
import com.github.lastsunday.moon.data.mapper.UserRoleMapper;
import com.github.lastsunday.moon.util.StringUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	protected UserMapper userMapper;
	@Autowired
	protected UserRoleMapper userRoleMapper;
	@Autowired
	protected RolePermissionMapper rolePermissionMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDO user = userMapper.select(username);
		if (StringUtils.isNull(user)) {
			log.info("login user: {} not exists", username);
			throw new UsernameNotFoundException("login user: " + username + " not exists");
		} else if (UserStatusType.DISABLE.getCode() == user.getStatus()) {
			log.info("login user: {} disable.", username);
			throw new UsernameNotFoundException("login user: " + username + " disable");
		}
		List<UserRoleDO> userRolelist = userRoleMapper.selectUserRoleByUserId(user.getId());
		Set<String> permissions = new HashSet<String>();
		for (UserRoleDO userRoleDo : userRolelist) {
			List<RolePermissionDO> rolePermissionList = rolePermissionMapper
					.selectRolePermissionByRoleId(userRoleDo.getRoleId());
			for (RolePermissionDO rolePermissionDo : rolePermissionList) {
				permissions.add(rolePermissionDo.getPermission());
			}
		}
		return createLoginUser(user, permissions);
	}

	public UserDetails createLoginUser(UserDO user, Set<String> permissions) {
		LoginUser loginUser = new LoginUser();
		loginUser.setId(user.getId());
		loginUser.setAccount(user.getAccount());
		loginUser.setPassword(user.getPassword());
		loginUser.setPermissions(permissions);
		return loginUser;
	}
}
