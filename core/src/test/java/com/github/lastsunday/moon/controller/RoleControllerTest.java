package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.*;
import com.github.lastsunday.service.core.ErrorResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerTest extends AbstractControllerTest {

    @Test
    public void testDeleteUsedRole() throws Exception {
        loginAsAdmin();
        //create role test
        RoleCreateParamDTO roleCreateParam = new RoleCreateParamDTO();
        roleCreateParam.setName("test");
        roleCreateParam.setStatus(0);
        RoleCreateResultDTO createRoleResponse = readResponse(doPost("/api/system/role/create", roleCreateParam).andExpect(status().isOk()), RoleCreateResultDTO.class);
        String createRoleId = createRoleResponse.getId();
        //create user test ref role test
        UserCreateParamDTO userCreateParam = new UserCreateParamDTO();
        userCreateParam.setName("test");
        userCreateParam.setAccount("test");
        userCreateParam.setPassword("test");
        userCreateParam.setRoleIds(Arrays.asList(createRoleId));
        UserCreateResultDTO createUserResponse = readResponse(doPost("/api/system/user/create", userCreateParam).andExpect(status().isOk()), UserCreateResultDTO.class);
        //try to delete role test
        RoleDeleteParamDTO param = new RoleDeleteParamDTO();
        param.setId(createRoleId);
        ErrorResponse errorResponse = readResponse(doPost("/api/system/role/delete", param).andExpect(status().isBadRequest()), ErrorResponse.class);
        Assert.assertEquals(RoleExceptionConstant.ERROR_CODE_ROLE_DELETE_FAILURE_BY_OTHER_USER_REF, errorResponse.getErrorCode());
    }

    @Test
    public void testDeleteRootRole() throws Exception {
        loginAsAdmin();
        RoleDeleteParamDTO param = new RoleDeleteParamDTO();
        param.setId("ACLYdWuNqIQOMkr9CgJxD");
        ErrorResponse errorResponse = readResponse(doPost("/api/system/role/delete", param).andExpect(status().isBadRequest()), ErrorResponse.class);
        Assert.assertEquals(RoleExceptionConstant.ERROR_CODE_DELETE_FAILURE, errorResponse.getErrorCode());
    }

    @Test
    public void testUpdateRootRole() throws Exception {
        loginAsAdmin();
        RoleUpdateParamDTO param = new RoleUpdateParamDTO();
        param.setId("ACLYdWuNqIQOMkr9CgJxD");
        param.setStatus(0);
        param.setName("ModifyRoot");
        ErrorResponse errorResponse = readResponse(doPost("/api/system/role/update", param).andExpect(status().isBadRequest()), ErrorResponse.class);
        Assert.assertEquals(RoleExceptionConstant.ERROR_CODE_UPDATE_FAILURE, errorResponse.getErrorCode());
    }

}
