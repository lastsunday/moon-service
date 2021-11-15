package com.github.lastsunday.moon.data.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lastsunday.moon.MainApplication;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.data.domain.RolePermissionDO;
import com.github.lastsunday.moon.util.IdGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
public class RolePermissionTest {

	@Autowired
	protected RoleMapper roleMapper;

	@Autowired
	protected RolePermissionMapper rolePermission;

	@Test
	public void addRolePermission() {
		RoleDO roleDo = new RoleDO();
		String roleId = IdGenerator.genUniqueStringId();
		roleDo.setId(roleId);
		roleDo.setName("SuperAdmin"+roleId);
		roleDo.setDesc("SuperAdmin"+roleId);
		roleMapper.insert(roleDo);
		RolePermissionDO rolePermissionDo = new RolePermissionDO();
		rolePermissionDo.setId(IdGenerator.genUniqueStringId());
		rolePermissionDo.setRoleId(roleDo.getId());
		rolePermissionDo.setPermission("*");
		rolePermission.insert(rolePermissionDo);
	}

}
