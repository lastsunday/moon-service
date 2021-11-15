package com.github.lastsunday.moon.controller.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.github.lastsunday.moon.dto.SortOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public abstract class AbstractPageParamDTO<T> {

	@NotNull
	@Min(1)
	@ApiModelProperty(example = "1")
	private Integer pageNum;
	@Min(1)
	@Max(1000)
	@NotNull
	@ApiModelProperty(example = "20")
	private Integer pageSize;
	// sort field
	private T sortField;
	// sort order
	private SortOrder sortOrder;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public T getSortField() {
		return sortField;
	}

	public void setSortField(T sortField) {
		this.sortField = sortField;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

}
