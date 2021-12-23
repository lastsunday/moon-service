package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.controller.dto.OperationLogListParamDTO;
import com.github.lastsunday.moon.controller.dto.PageResultDTO;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LogControllerTest extends AbstractControllerTest {
    @Test
    public void testDeleteUsedRole() throws Exception {
        loginAsAdmin();
        OperationLogListParamDTO param = new OperationLogListParamDTO();
        param.setPageNum(1);
        param.setPageSize(20);
        readResponse(doPost("/api/system/log/operationLogList", param).andExpect(status().isOk()), PageResultDTO.class);
    }
}
