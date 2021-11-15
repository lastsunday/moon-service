package com.github.lastsunday.moon.data.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lastsunday.moon.MainApplication;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.util.IdGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleMapperTest {

	@Autowired
	protected RoleMapper roleMapper;
	
	@Test
	public void testAdd() {
		RoleDO roleDo = new RoleDO();
		roleDo.setId(IdGenerator.genUniqueStringId());
		roleDo.setDesc("SuperAdmin");
		roleDo.setName("SuperAdmin");
		roleMapper.insert(roleDo);
	}
}
