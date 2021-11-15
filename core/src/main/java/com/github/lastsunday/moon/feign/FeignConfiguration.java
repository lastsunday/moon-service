package com.github.lastsunday.moon.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.lastsunday.moon.config.AppConfig;
import com.github.lastsunday.moon.config.app.remote.Service;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;

@Configuration
@Import(FeignClientsConfiguration.class)
@ConditionalOnProperty(prefix = "app.remote.service", value = "enable", havingValue = "true")
public class FeignConfiguration {

	@Autowired
	protected AppConfig appConfig;

	@Bean
	public RemoteServiceClient remoteServiceClient(Contract contract, Decoder decoder, Encoder encoder) {
		Service service = appConfig.getRemote().getService();
		return Feign.builder().contract(contract).encoder(encoder).decoder(decoder)
				.requestInterceptor(new AuthRequestInterceptor(service.getAuthHeader(), service.getAuthToken()))
				.target(RemoteServiceClient.class, service.getUrl());
	}

}
