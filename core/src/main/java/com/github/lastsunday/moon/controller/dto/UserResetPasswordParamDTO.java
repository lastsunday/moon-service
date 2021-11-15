package com.github.lastsunday.moon.controller.dto;

import javax.validation.constraints.NotBlank;

public class UserResetPasswordParamDTO {

	@NotBlank
	private String userId;
	@NotBlank
	private String newPassword;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
