package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.*;
import com.github.lastsunday.moon.util.UUID;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

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
        //create user test
        UserCreateParamDTO userCreateParam = new UserCreateParamDTO();
        String testAccount = "test" + UUID.shortUUID();
        String oldPassword = testAccount + "123";
        userCreateParam.setName(testAccount);
        userCreateParam.setAccount(testAccount);
        userCreateParam.setPassword(oldPassword);
        userCreateParam.setRoleIds(Arrays.asList("ACLYdWuNqIQOMkr9CgJxD"));
        UserCreateResultDTO createUserResponse = readResponse(doPost("/api/system/user/create", userCreateParam).andExpect(status().isOk()), UserCreateResultDTO.class);
        //logout
        doPost("/api/client/logout").andExpect(status().isOk());
        //login as test account
        login(testAccount, oldPassword);
        String newPassword = testAccount + "1234";
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
        clientLoginParamDTO.setAccount(testAccount);
        clientLoginParamDTO.setPassword(oldPassword);
        doPost("/api/client/login", clientLoginParamDTO).andExpect(status().isBadRequest());
        login(testAccount, newPassword);
        ClientInfoResultDTO clientInfoResultDTO = readResponse(doPost("/api/client/info").andExpect(status().isOk()), ClientInfoResultDTO.class);
        Assert.assertEquals(testAccount, clientInfoResultDTO.getName());
    }

}
