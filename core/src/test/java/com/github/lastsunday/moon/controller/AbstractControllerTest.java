package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.ClientLoginParamDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginResultDTO;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AbstractControllerTest.class, loader = SpringBootContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Configuration
@ComponentScan({"com.github.lastsunday.moon"})
@WebAppConfiguration
@SpringBootTest()
public class AbstractControllerTest extends AbstractWebTest {

    protected void loginAsAdmin() throws Exception {
        login("admin", "admin123");
    }

    protected void login(String account, String password) throws Exception {
        this.token = null;
        ClientLoginParamDTO param = new ClientLoginParamDTO();
        param.setAccount(account);
        param.setPassword(password);
        ClientLoginResultDTO clientLoginResultDTO = readResponse(doPost("/api/client/login", param).andExpect(status().isOk()), ClientLoginResultDTO.class);
        this.token = clientLoginResultDTO.getToken();
    }
}
