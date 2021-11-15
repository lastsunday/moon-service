package com.github.lastsunday.moon.config.app;

import com.github.lastsunday.moon.config.app.service.Token;

public class Service {

	private boolean showErrorDetail;

	private Token token = new Token();

	public boolean isShowErrorDetail() {
		return showErrorDetail;
	}

	public void setShowErrorDetail(boolean showErrorDetail) {
		this.showErrorDetail = showErrorDetail;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
}
