package com.github.lastsunday.moon.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.lastsunday.moon.util.SecurityUtils;
import com.github.lastsunday.moon.util.StringUtils;

import io.jsonwebtoken.MalformedJwtException;

/**
 * token过滤器 验证token有效性
 * 
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	public static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

	@Autowired
	private TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			LoginUser loginUser = tokenService.getLoginUser(request);
			if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
				tokenService.verifyToken(loginUser);
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						loginUser, null, loginUser.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			chain.doFilter(request, response);
		} catch (MalformedJwtException e) {
			log.debug("invalid jwt content", e);
			chain.doFilter(request, response);
		}
	}
}
