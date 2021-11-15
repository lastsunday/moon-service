package com.github.lastsunday.service.core.security;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.github.lastsunday.moon.controller.AbstractController;

@Component
public class PermissionManager {

	protected Logger log = LoggerFactory.getLogger(PermissionManager.class);
	private List<String> allPermissionList = new ArrayList<String>();

	public void fetchSystemPermission() {
		log.info("fetchSystemPermission start");
		try {
			List<Class<?>> classList = new ArrayList<>();
			List<Class<?>> serviceClassArray = getAllActionSubClass(AbstractController.class.getName());
			for (Class<?> serviceClass : serviceClassArray) {
				Class<?>[] interfaces = serviceClass.getInterfaces();
				if (interfaces != null && interfaces.length > 0) {
					for (Class<?> clazz : interfaces) {
						classList.add(clazz);
					}
				}
			}
			for (Class<?> clazz : classList) {
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					PreAuthorize annotaionClass = (PreAuthorize) method.getAnnotation(PreAuthorize.class);
					if (annotaionClass != null) {
						allPermissionList.add(convertAnnotationPermissionString(annotaionClass.value()));
					} else {
						// skip
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.info("permission list size = " + allPermissionList.size());
		log.info(Arrays.toString(allPermissionList.toArray()));
		log.info("fetchSystemPermission end.");
	}

	public static List<Class<?>> getAllActionSubClass(String classPackageAndName) throws Exception {
		Field field = null;
		Vector<?> v = null;
		Class<?> cls = null;
		List<Class<?>> allSubclass = new ArrayList<Class<?>>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> classOfClassLoader = classLoader.getClass();
		cls = Class.forName(classPackageAndName);
		while (classOfClassLoader != ClassLoader.class) {
			classOfClassLoader = classOfClassLoader.getSuperclass();
		}
		field = classOfClassLoader.getDeclaredField("classes");
		field.setAccessible(true);
		v = (Vector<?>) field.get(classLoader);
		for (int i = 0; i < v.size(); ++i) {
			Class<?> c = (Class<?>) v.get(i);
			if (cls.isAssignableFrom(c) && !cls.equals(c)) {
				allSubclass.add((Class<?>) c);
			}
		}
		return allSubclass;
	}

	protected String convertAnnotationPermissionString(String str) {
		return str.substring("@ss.hasPermission('".length(), str.lastIndexOf("')"));
	}

	public List<String> getAllPermissions() {
		return allPermissionList;
	}

	public static boolean hasPermission(List<String> ownPermissionList, String permission) {
		// 拥有的权限
		boolean hasPermission = false;
		// 目标匹配权限
		String[] permissions = permission.split(":");
		outer: for (String item : ownPermissionList) {
			// 拥有的权限点
			String[] hasPermissionString = item.split(":");
			inner: for (int i = 0; i < permissions.length; i++) {
				if (i >= hasPermissionString.length) {
					break inner;
				} else if ("*".equals(hasPermissionString[i])) {
					hasPermission = true;
					break outer;
				} else if (hasPermissionString[i].equals(permissions[i])) {
					// 此层合格
					if (i == permissions.length - 1) {
						// 若是目标层的最后一层。则表示所有层校验通过
						hasPermission = true;
					}
				} else {
					break inner;
				}
			}
		}
		return hasPermission;
	}
}
