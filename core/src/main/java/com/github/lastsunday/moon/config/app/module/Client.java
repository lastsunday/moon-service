package com.github.lastsunday.moon.config.app.module;

public class Client {

	private boolean loginCaptchaCheckingEnable = true;

	public boolean isLoginCaptchaCheckingEnable() {
		return loginCaptchaCheckingEnable;
	}

	public void setLoginCaptchaCheckingEnable(boolean loginCaptchaCheckingEnable) {
		this.loginCaptchaCheckingEnable = loginCaptchaCheckingEnable;
	}

}
