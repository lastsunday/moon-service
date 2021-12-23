package com.github.lastsunday.moon.data.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisAutoConfig {

	/**** 缓存专用数据源 ****/
	@Bean
	public LettuceConnectionFactory defaultLettuceConnectionFactory(RedisStandaloneConfiguration defaultRedisConfig,
			@SuppressWarnings("rawtypes") GenericObjectPoolConfig defaultPoolConfig) {
		LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(5000)).poolConfig(defaultPoolConfig).build();
		return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
	}

	@Bean
	public StringRedisTemplate redisTemplate(LettuceConnectionFactory defaultLettuceConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(defaultLettuceConnectionFactory);
		return redisTemplate;
	}

	@Configuration
	public static class DefaultRedisConfig {
		@Value("${spring.redis.host:127.0.0.1}")
		private String host;
		@Value("${spring.redis.port:6379}")
		private Integer port;
		@Value("${spring.redis.password:}")
		private String password;
		/*
		* When using Redis Cluster, the SELECT command cannot be used, since Redis Cluster only supports database zero.
		* In the case of a Redis Cluster, having multiple databases would be useless and an unnecessary source of complexity.
		* Commands operating atomically on a single database would not be possible with the Redis Cluster design and goals.
		* */
		@Value("${spring.redis.database:0}")
		private Integer database;

		@Value("${spring.redis.lettuce.pool.max-active:8}")
		private Integer maxActive;
		@Value("${spring.redis.lettuce.pool.max-idle:8}")
		private Integer maxIdle;
		@Value("${spring.redis.lettuce.pool.max-wait:-1}")
		private Long maxWait;
		@Value("${spring.redis.lettuce.pool.min-idle:0}")
		private Integer minIdle;

		@SuppressWarnings("rawtypes")
		@Bean
		public GenericObjectPoolConfig defaultPoolConfig() {
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMaxTotal(maxActive);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			config.setMaxWaitMillis(maxWait);
			return config;
		}

		@Bean
		public RedisStandaloneConfiguration defaultRedisConfig() {
			RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
			config.setHostName(host);
			config.setPassword(RedisPassword.of(password));
			config.setPort(port);
			config.setDatabase(database);
			return config;
		}
	}
}