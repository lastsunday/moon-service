package com.github.lastsunday.moon.data.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lastsunday.moon.data.DateRange;
import com.github.lastsunday.moon.data.domain.OperationLogDO;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.dto.SortOrder;

import java.util.Date;
import java.util.List;

public interface OperationLogMapper extends BaseMapper<OperationLogDO> {

    public static enum SortField {
        CREATE_TIME
    }

    default List<OperationLogDO> selectList(String account, String ip, DateRange createTimeRange, Integer functionModule,
                                            Integer operation, Integer errorCode, Boolean notHasErrorCode,
                                            String request, String response, SortField sortField, SortOrder sortOrder) {
        OperationLogDO param = new OperationLogDO();
        param.setOperatorAccount(account);
        param.setIp(ip);
        param.setErrorCode(errorCode);
        param.setFunctionModule(functionModule);
        param.setOperation(operation);
        QueryWrapper<OperationLogDO> queryWrapper = new QueryWrapper<>(param);
        queryWrapper.between(createTimeRange.isValid(), "create_time", createTimeRange.getStartDate(), createTimeRange.getEndDate());
        queryWrapper.like(request != null, "request", request);
        queryWrapper.like(response != null, "response", response);
        queryWrapper.isNull(notHasErrorCode, "error_code");
        queryWrapper.orderBy(sortField != null, SortOrder.ASC.equals(sortOrder), "create_time");
        queryWrapper.orderBy(sortField == null, false, "create_time");
        return selectList(queryWrapper);
    }
}
