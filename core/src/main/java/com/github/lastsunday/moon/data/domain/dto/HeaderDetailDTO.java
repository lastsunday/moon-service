package com.github.lastsunday.moon.data.domain.dto;

import java.util.List;

public abstract class HeaderDetailDTO<T>{

	private List<T> items;

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
}
