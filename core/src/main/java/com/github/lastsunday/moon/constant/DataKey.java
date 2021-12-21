package com.github.lastsunday.moon.constant;

public interface DataKey {
    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_CACHE_KEY = "captcha:code:";

    /**
     * 登录用户 redis key
     */
    String USER_LOGIN_TOKEN_CACHE_KEY = "user:login_token:";

    String LOGIN_USER_CLAIM_KEY = "u";

    String LOCK_KEY_PREFIX = "l:";
}
