package com.github.lastsunday.moon.controller.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.lastsunday.moon.controller.dto.UserOnlineListParamDTO.SortField;

public class UserOnlineListParamDTO extends AbstractPageParamDTO<SortField> {

	private String ipaddr;
	private String userName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private List<Date> loginRangeDate;

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Date> getLoginRangeDate() {
		return loginRangeDate;
	}

	public void setLoginRangeDate(List<Date> loginRangeDate) {
		this.loginRangeDate = loginRangeDate;
	}

	public static enum SortField {
		LOGIN_TIME
	}
}
