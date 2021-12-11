package com.github.lastsunday.moon.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.github.lastsunday.moon.config.AppConfig;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

	protected Logger log = LoggerFactory.getLogger(SecuritySecureConfig.class);

	@Autowired
	protected AppConfig appConfig;

	@Autowired
	private JwtAuthenticationTokenFilter authenticationTokenFilter;

	/**
	 * 自定义用户认证逻辑
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 解决 无法直接注入 AuthenticationManager
	 *
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		if (appConfig.getSwagger().isEnable()) {
			log.info("Permit swagger resource path");
			http.authorizeRequests().antMatchers("/swagger-ui.html").permitAll()
					//
					.and().authorizeRequests().antMatchers("/swagger-ui/**").permitAll()
					//
					.and().authorizeRequests().antMatchers("/v3/api-docs/**").permitAll();
		} else {
			// skip
		}
		http.authorizeRequests().and().authorizeRequests()
				//
				.antMatchers("/").permitAll().and().authorizeRequests()
				// 验证码
				.antMatchers("/api/captcha").permitAll()
				//
				.antMatchers("/index.html").permitAll()
				//
				.antMatchers("/admin/**").permitAll()
				//
				.antMatchers("/app/**").permitAll()
				//
				.antMatchers("/mini-app/**").permitAll()
				//
				.antMatchers("/repository/**").permitAll()
				//
				.and().authorizeRequests().antMatchers("/error").permitAll()
				//
				.antMatchers("/api/common/**").permitAll()
				//
				.and().authorizeRequests().antMatchers("/api/client/login").permitAll()
				//
				.and().addFilterAfter(authenticationTokenFilter, BasicAuthenticationFilter.class).authorizeRequests()
				//
				.and().authorizeRequests().anyRequest().authenticated();
	}

	/**
	 * 强散列哈希加密实现
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 身份认证接口
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}