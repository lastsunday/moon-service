package com.github.lastsunday.moon.config.log;

import com.alibaba.fastjson.JSON;
import com.github.lastsunday.moon.security.LoginUser;
import com.github.lastsunday.moon.security.TokenService;
import com.github.lastsunday.service.core.CommonException;
import com.github.lastsunday.service.core.manager.AsyncManager;
import io.swagger.v3.oas.annotations.Operation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 统一日志处理切面
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    @Autowired
    private TokenService tokenService;

    @Pointcut("execution(public * com.github.lastsunday.moon.controller.*Impl.*(..)) || execution(public * com.github.lastsunday.service.core.controller.*Impl.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录请求信息(通过Logstash传入Elasticsearch)
        final WebLog webLog = new WebLog();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(Operation.class)) {
            Operation log = method.getAnnotation(Operation.class);
            webLog.setDescription(log.description());
        }
        String urlStr = request.getRequestURL().toString();
        webLog.setBasePath(urlStr);
        webLog.setIp(IpUtils.getIpAddr(request));
        webLog.setMethod(request.getMethod());
        webLog.setParameter(getParameter(method, joinPoint.getArgs()));
        webLog.setStartTime(startTime);
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(request.getRequestURL().toString());
        webLog.setThreadId(Thread.currentThread().getId());
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser != null) {
            webLog.setUserId(loginUser.getId());
            webLog.setUserAccount(loginUser.getAccount());
        } else {
            // skip
        }
        final WebLog target = new WebLog();
        BeanUtils.copyProperties(webLog, target);
        log.info("request {}", JSON.toJSONString(target));
        try {
            Object result = joinPoint.proceed();
            if (isNotSetResult(result)) {
                // skip
            } else {
                webLog.setResult(result);
            }
            webLog.setError(false);
//			Map<String, Object> logMap = new HashMap<>();
//			logMap.put("url", webLog.getUrl());
//			logMap.put("method", webLog.getMethod());
//			logMap.put("parameter", webLog.getParameter());
//			logMap.put("spendTime", webLog.getSpendTime());
//			logMap.put("description", webLog.getDescription());
//			log.info("{}", JSON.toJSONString(webLog));
//          log.info(Markers.appendEntries(logMap), JSONUtil.parse(webLog).toString());
            return result;
        } catch (Exception e) {
            ExceptionLog exceptionLog = new ExceptionLog();
            exceptionLog.setErrorDetail(substring(e.getMessage(), 1000));
            if (exceptionLog.getErrorDetail() == null || exceptionLog.getErrorDetail().isEmpty()) {
                exceptionLog.setErrorDetail("exception class = " + e.getClass().getName());
            } else {
                // skip
            }
            if (e instanceof CommonException) {
                CommonException controllerException = (CommonException) e;
                exceptionLog.setErrorCode(controllerException.getErrorCode());
                exceptionLog.setErrorMessage(controllerException.getErrorMessage());
            } else {
                // skip
            }
            webLog.setResult(exceptionLog);
            webLog.setError(true);
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            webLog.setEndTime(endTime);
            webLog.setSpendTime(endTime - startTime);
            log.debug("response {}", JSON.toJSONString(webLog));
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean isNotSetResult(Object object) {
        if (object instanceof ResponseEntity) {
            if (((ResponseEntity) object).getBody() instanceof InputStreamResource) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static final String NULLSTR = "";

    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }
        return str.substring(start);
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            // 将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (StringUtils.hasText(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}