package com.github.lastsunday.moon.config.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.lastsunday.moon.config.log.emun.FunctionModule;
import com.github.lastsunday.moon.config.log.emun.Operation;

/**
 * 操作日志注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
	/**
	 * 模块
	 */
	public FunctionModule functionModule() default FunctionModule.UNSPECIFIED;

	/**
	 * 操作人类别
	 */
	public Operation operation() default Operation.UNSPECIFIED;

}
