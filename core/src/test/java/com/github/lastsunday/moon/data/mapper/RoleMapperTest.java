package com.github.lastsunday.moon.data.mapper;

import com.github.lastsunday.moon.data.domain.RoleDO;
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
@ContextConfiguration(classes = RoleMapperTest.class, loader = SpringBootContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Configuration
@ComponentScan({"com.github.lastsunday.moon"})
@WebAppConfiguration
@SpringBootTest()
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
