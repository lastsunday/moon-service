package com.github.lastsunday.moon.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.lastsunday.moon.data.domain.dto.RoleDTO;

@TableName("role")
public class RoleDO extends DefaultDO<RoleDTO> {

	private String name;
	@TableField("`desc`")
	private String desc;
	/** 0.冻结 1.激活 */
	private Integer status;
	@TableField("can_modify")
	private Boolean canModify;
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getCanModify() {
		return canModify;
	}

	public void setCanModify(Boolean canModify) {
		this.canModify = canModify;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public RoleDTO toData() {
		RoleDTO result = new RoleDTO();
		fillData(result);
		result.setName(getName());
		result.setDesc(getDesc());
		result.setStatus(getStatus());
		result.setCanModify(getCanModify());
		result.setRemark(getRemark());
		return result;
	}
}
