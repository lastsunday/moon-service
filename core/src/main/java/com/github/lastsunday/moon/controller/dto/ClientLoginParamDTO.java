package com.github.lastsunday.moon.controller.dto;

import javax.validation.constraints.NotBlank;

public class ClientLoginParamDTO {

	@NotBlank
	private String account;

	@NotBlank
	private String password;

	private String uuid;

	private String verifyCode;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

}
