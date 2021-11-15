package com.github.lastsunday.moon.controller.dto;

import java.util.List;

public abstract class HeaderDetailResultDTO<T>{

	private List<T> items;

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
}
