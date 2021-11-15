package com.github.lastsunday.moon.config.app.service;

public class Token {

	private String header = "Authorization";
	private String secret = "6711b7cbfe8551fefc60e7d9bd44da4f";
	// 960min
	private Integer expireTime = 960;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Integer expireTime) {
		this.expireTime = expireTime;
	}
}
