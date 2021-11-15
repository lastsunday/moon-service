package com.github.lastsunday.moon.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.BeanUtils;

import com.github.pagehelper.Page;
import com.github.lastsunday.moon.controller.dto.HeaderDetailResultDTO;
import com.github.lastsunday.moon.controller.dto.PageResultDTO;
import com.github.lastsunday.moon.data.domain.DefaultDO;
import com.github.lastsunday.moon.data.domain.dto.HeaderDetailDTO;
import com.github.lastsunday.moon.util.SecurityUtils;
import com.github.lastsunday.service.core.controller.SwaggerConstant;

public interface AbstractController extends SwaggerConstant {

	default void fillBaseInfoForCreateDefaultDto(DefaultDO defaultDo) {
		defaultDo.setCreateBy(SecurityUtils.getLoginUser().getAccount());
		defaultDo.setCreateTime(new Date());
	}

	default void fillBaseInfoForUpdateDefaultDto(DefaultDO defaultDo) {
		defaultDo.setUpdateBy(SecurityUtils.getLoginUser().getAccount());
		defaultDo.setUpdateTime(new Date());
	}

	default <S, T> List<T> copyList(List<S> sources, Supplier<T> target) {
		List<T> list = new ArrayList<>(sources.size());
		for (S source : sources) {
			T t = target.get();
			BeanUtils.copyProperties(source, t);
			list.add(t);
		}
		return list;
	}

	default <S, T> PageResultDTO<T> copyAndReturn(List<S> list, Supplier<T> target) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageResultDTO<T> result = new PageResultDTO(copyList(list, target));
		BeanUtils.copyProperties(list, result);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <S, T extends PageResultDTO, U> T copyAndReturn(List<S> list, Supplier<T> target, Supplier<U> targetItem) {
		T result = (T) target.get();
		List<U> items = new ArrayList<>(list.size());
		for (S source : list) {
			U t = targetItem.get();
			BeanUtils.copyProperties(source, t);
			items.add(t);
		}
		result.setItems(items);
		BeanUtils.copyProperties(list, result);
		if (list instanceof Page) {
			Page page = (Page) list;
			result.setEndRow(page.getEndRow());
			result.setPageNum(page.getPageNum());
			result.setPages(page.getPages());
			result.setPageSize(page.getPageSize());
			result.setStartRow(page.getStartRow());
			result.setTotal(page.getTotal());
		} else {
			// skip
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	default <S, T extends PageResultDTO, U> T copyAndReturnPage(List<S> list, T targetInstance,
			Supplier<U> targetItem) {
		return copyAndReturnPage(list, targetInstance, targetItem, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <S, T extends PageResultDTO, U> T copyAndReturnPage(List<S> list, T targetInstance, Supplier<U> targetItem,
			ListCopyCallback<U, S> callback) {
		List<U> items = new ArrayList<>(list.size());
		for (S source : list) {
			U t = targetItem.get();
			BeanUtils.copyProperties(source, t);
			if (callback != null) {
				callback.onCopy(t, source);
			} else {
				// skip
			}
			items.add(t);
		}
		targetInstance.setItems(items);
		BeanUtils.copyProperties(list, targetInstance);
		if (list instanceof Page) {
			Page page = (Page) list;
			targetInstance.setEndRow(page.getEndRow());
			targetInstance.setPageNum(page.getPageNum());
			targetInstance.setPages(page.getPages());
			targetInstance.setPageSize(page.getPageSize());
			targetInstance.setStartRow(page.getStartRow());
			targetInstance.setTotal(page.getTotal());
		} else {
			// skip
		}
		return targetInstance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <T extends HeaderDetailResultDTO, U> T copyAndReturn(HeaderDetailDTO<?> dto, Supplier<T> target,
			Supplier<U> targetItem) {
		T result = (T) target.get();
		List list = dto.getItems();
		List<U> items = new ArrayList<>(list.size());
		for (Object source : dto.getItems()) {
			U t = targetItem.get();
			BeanUtils.copyProperties(source, t);
			items.add(t);
		}
		result.setItems(items);
		BeanUtils.copyProperties(dto, result);
		return result;
	}

	default <S, T> T copyAndReturn(S source, Supplier<T> target) {
		return copyAndReturn(source, target, null);
	}

	default <S, T> T copyAndReturn(S source, Supplier<T> target, CopyCallback<T, S> callback) {
		T result = target.get();
		BeanUtils.copyProperties(source, result);
		if (callback != null) {
			callback.onCopy(result, source);
		} else {
			// skip
		}
		return result;
	}

	default <S, T> void copy(S source, T target) {
		BeanUtils.copyProperties(source, target);
	}

	public static interface CopyCallback<T, S> {
		void onCopy(T t, S s);
	}

	public static interface ListCopyCallback<T, S> {
		void onCopy(T t, S s);
	}
}
