package com.github.lastsunday.moon.data.mapper;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageHelper;
import com.github.lastsunday.moon.MainApplication;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.data.domain.RolePermissionDO;
import com.github.lastsunday.moon.data.domain.UserDO;
import com.github.lastsunday.moon.data.domain.UserRoleDO;
import com.github.lastsunday.moon.data.domain.dto.UserDTO;
import com.github.lastsunday.moon.data.mapper.user.SelectUserDetailParam;
import com.github.lastsunday.moon.util.IdGenerator;
import com.github.lastsunday.moon.util.SecurityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
public class UserMapperTest {

	@Autowired
	protected RoleMapper roleMapper;
	@Autowired
	protected RolePermissionMapper rolePermission;
	@Autowired
	protected UserMapper userMapper;
	@Autowired
	protected UserRoleMapper userRoleMapper;

	@Test
	public void testAdd() {
		RoleDO roleDo = new RoleDO();
		String roleId = IdGenerator.genUniqueStringId();
		roleDo.setId(roleId);
		roleDo.setName("Guest" + roleId);
		roleDo.setDesc("Guest" + roleId);
		roleMapper.insert(roleDo);
		RolePermissionDO rolePermissionDo = new RolePermissionDO();
		rolePermissionDo.setId(IdGenerator.genUniqueStringId());
		rolePermissionDo.setRoleId(roleDo.getId());
		rolePermissionDo.setPermission("*");
		rolePermission.insert(rolePermissionDo);
		UserDO userDo = new UserDO();
		String userId = IdGenerator.genUniqueStringId();
		userDo.setId(userId);
		userDo.setAccount("admin" + new Date().getTime());
		userDo.setName("admin" + new Date().getTime());
		userDo.setPassword(SecurityUtils.encryptPassword("guest123"));
		userMapper.insert(userDo);
		UserRoleDO userRoleDo = new UserRoleDO();
		userRoleDo.setId(IdGenerator.genUniqueStringId());
		userRoleDo.setRoleId(roleDo.getId());
		userRoleDo.setUserId(userDo.getId());
		userRoleMapper.insert(userRoleDo);
	}

	@Test
	public void testSelectUserDetail() {
		PageHelper.startPage(1, 10);
		List<UserDTO> list = userMapper.selectUserDetail(null);
		System.out.println(list.size());
	}

	@Test
	public void testSelectUserDetailForSuperAdminRole() {
		PageHelper.startPage(1, 10);
		SelectUserDetailParam param = new SelectUserDetailParam();
		param.setAccount("admin");
		param.getRoleIds().add("ACLYdWuNqIQOMkr9CgJxD");
		List<UserDTO> list = userMapper.selectUserDetail(param);
		UserDTO userDto = list.get(0);
		assertEquals("admin", userDto.getAccount());
		assertTrue(userDto.getRoleIds().contains("ACLYdWuNqIQOMkr9CgJxD"));
	}

	@Test
	public void testSelectUserDetailForSuperAdminRoleWithAccount() {
		PageHelper.startPage(1, 10);
		SelectUserDetailParam param = new SelectUserDetailParam();
		param.setAccount("admin");
		List<UserDTO> list = userMapper.selectUserDetail(param);
		UserDTO userDto = list.get(0);
		assertTrue(userDto.getAccount().startsWith("admin"));
	}

	@Test
	public void testSelectUserDetailForSuperAdminRoleWithRoleId() {
		PageHelper.startPage(1, 10);
		SelectUserDetailParam param = new SelectUserDetailParam();
		param.getRoleIds().add("ACLYdWuNqIQOMkr9CgJxD");
		List<UserDTO> list = userMapper.selectUserDetail(param);
		UserDTO userDto = list.get(0);
		assertTrue(userDto.getRoleIds().contains("ACLYdWuNqIQOMkr9CgJxD"));
	}
}
