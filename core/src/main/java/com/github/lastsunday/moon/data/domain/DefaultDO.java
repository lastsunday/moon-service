package com.github.lastsunday.moon.data.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.lastsunday.moon.data.ToData;
import com.github.lastsunday.moon.data.domain.dto.DefaultDTO;

public abstract class DefaultDO<T> implements ToData<T> {

	@TableId
	private String id;

	/** 创建者 */
	@TableField("create_by")
	private String createBy;

	/** 创建时间 */
	@TableField("create_time")
	private Date createTime;

	/** 更新者 */
	@TableField("update_by")
	private String updateBy;

	/** 更新时间 */
	@TableField("update_time")
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void fillData(DefaultDTO dto){
		dto.setId(getId());
		dto.setCreateBy(getCreateBy());
		dto.setCreateTime(getCreateTime());
		dto.setUpdateBy(getUpdateBy());
		dto.setUpdateTime(getUpdateTime());
	}
}
