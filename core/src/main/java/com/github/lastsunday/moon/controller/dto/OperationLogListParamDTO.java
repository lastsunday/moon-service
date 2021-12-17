package com.github.lastsunday.moon.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class OperationLogListParamDTO extends AbstractPageParamDTO<OperationLogListParamDTO.SortField> {
    private String operatorAccount;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> createTime;
    private Integer functionModule;
    private Integer operation;
    private Integer errorCode;
    private Boolean notHasErrorCode;
    private String request;
    private String response;

    public String getOperatorAccount() {
        return operatorAccount;
    }

    public void setOperatorAccount(String operatorAccount) {
        this.operatorAccount = operatorAccount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }

    public Integer getFunctionModule() {
        return functionModule;
    }

    public void setFunctionModule(Integer functionModule) {
        this.functionModule = functionModule;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Boolean getNotHasErrorCode() {
        return notHasErrorCode;
    }

    public void setNotHasErrorCode(Boolean notHasErrorCode) {
        this.notHasErrorCode = notHasErrorCode;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static enum SortField {
        CREATE_TIME
    }
}
