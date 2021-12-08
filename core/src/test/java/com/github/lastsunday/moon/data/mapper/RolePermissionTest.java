package com.github.lastsunday.moon.data.mapper;

import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.data.domain.RolePermissionDO;
import com.github.lastsunday.moon.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration(classes = RolePermissionTest.class, loader = SpringBootContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Configuration
@ComponentScan({"com.github.lastsunday.moon"})
@WebAppConfiguration
@SpringBootTest()
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
