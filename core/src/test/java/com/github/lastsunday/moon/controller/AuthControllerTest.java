package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.ClientInfoResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginParamDTO;
import com.github.lastsunday.moon.controller.dto.ClientResetPasswordParamDTO;
import org.junit.Assert;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractControllerTest {

    @Test
    public void testLoginAdmin() throws Exception {
        loginAsAdmin();
        ClientInfoResultDTO clientInfoResultDTO = readResponse(doPost("/api/client/info").andExpect(status().isOk()), ClientInfoResultDTO.class);
        Assert.assertEquals("admin", clientInfoResultDTO.getName());
    }

    @Test
    public void testLogout() throws Exception {
        loginAsAdmin();
        doPost("/api/client/logout").andExpect(status().isOk());
        doPost("/api/client/info").andExpect(status().isUnauthorized());
    }

    @Test
    public void testResetPassword() throws Exception {
        loginAsAdmin();
        String account = "admin";
        String oldPassword = "admin123";
        String newPassword = "admin1234";
        //reset password error
        ClientResetPasswordParamDTO param = new ClientResetPasswordParamDTO();
        param.setCurrentPassword(newPassword);
        param.setNewPassword(oldPassword);
        //reset password
        doPost("/api/client/resetPassword", param).andExpect(status().isBadRequest());
        param.setCurrentPassword(oldPassword);
        param.setNewPassword(newPassword);
        doPost("/api/client/resetPassword", param).andExpect(status().isOk());
        //logout
        doPost("/api/client/logout").andExpect(status().isOk());
        //login with old password
        ClientLoginParamDTO clientLoginParamDTO = new ClientLoginParamDTO();
        clientLoginParamDTO.setAccount(account);
        clientLoginParamDTO.setPassword(oldPassword);
        doPost("/api/client/login", clientLoginParamDTO).andExpect(status().isBadRequest());
        login(account, newPassword);
        ClientInfoResultDTO clientInfoResultDTO = readResponse(doPost("/api/client/info").andExpect(status().isOk()), ClientInfoResultDTO.class);
        Assert.assertEquals(account, clientInfoResultDTO.getName());
    }

}
