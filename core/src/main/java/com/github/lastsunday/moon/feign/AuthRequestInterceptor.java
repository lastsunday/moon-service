package com.github.lastsunday.moon.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AuthRequestInterceptor implements RequestInterceptor {

	private String header;
	private String value;

	public AuthRequestInterceptor(String header, String value) {
		super();
		this.header = header;
		this.value = value;
	}

	@Override
	public void apply(RequestTemplate template) {
		template.header(header, value);
	}

}
