package com.github.lastsunday.moon.config.log.emun;

public enum Operation {

    //
    UNSPECIFIED(0),
    //
    ADD(10001),
    //
    DELETE(10002),
    //
    UPDATE(10003),
    //
    SEARCH(10004),
    //
    CLIENT_LOGIN(20001),
    //
    CLIENT_LOGOUT(20002),
    //
    CLIENT_RESET_PASSWORD(20003),
    //
    USER_FORCE_LOGOUT(30001),
    //
    USER_RESET_PASSWORD(30002),
    //
    LOG_GET(50001);

    private int code;

    private Operation(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
