package com.github.lastsunday.moon.controller.dto;

import javax.validation.constraints.NotBlank;

public class UserOnlineForceLogoutParamDTO {
	@NotBlank
	private String tokenId;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

}
