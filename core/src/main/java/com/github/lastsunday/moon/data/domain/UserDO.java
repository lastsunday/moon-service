package com.github.lastsunday.moon.data.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class UserDO extends DefaultDO {

	/** 用户账号 */
	private String account;

	/** 用户名称 */
	private String name;

	/** 用户类型（0系统用户） */
	private Integer type;

	/** 用户邮箱 */
	private String email;

	/** 手机号码 */
	private String phone;

	/** 用户性别 */
	private Integer gender;

	/** 用户头像 */
	private String avatar;

	/** 密码 */
	private String password;

	/** 帐号状态（0正常 1停用） */
	private Integer status;

	/** 最后登录IP */
	@TableField("login_ip")
	private String loginIp;

	/** 最后登录时间 */
	@TableField("login_time")
	private Date loginTime;

	/** 备注 */
	private String remark;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
