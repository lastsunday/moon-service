package com.github.lastsunday.service.core.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("ss")
public class SecurityService {

	protected Logger log = LoggerFactory.getLogger(SecurityService.this.getClass());

	@SuppressWarnings("unchecked")
	public boolean hasPermission(String permission) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return PermissionManager.hasPermission(
				convertGrantedAuthorityListToStringList((List<GrantedAuthority>) authentication.getAuthorities()),
				permission);
	}

	public List<String> convertGrantedAuthorityListToStringList(List<GrantedAuthority> list) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i).getAuthority());
		}
		return result;
	}

}
