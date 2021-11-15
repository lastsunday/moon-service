package com.github.lastsunday.moon.controller.dto;

import javax.validation.constraints.NotBlank;

public class RoleGetParamDTO {

	@NotBlank
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
