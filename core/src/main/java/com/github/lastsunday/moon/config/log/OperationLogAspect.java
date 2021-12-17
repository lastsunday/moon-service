package com.github.lastsunday.moon.config.log;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSON;
import com.github.lastsunday.moon.data.domain.OperationLogDO;
import com.github.lastsunday.moon.data.mapper.OperationLogMapper;
import com.github.lastsunday.moon.security.LoginUser;
import com.github.lastsunday.moon.security.TokenService;
import com.github.lastsunday.moon.util.IdGenerator;
import com.github.lastsunday.service.core.CommonException;
import com.github.lastsunday.service.core.manager.AsyncManager;
import com.github.lastsunday.service.core.util.SpringUtils;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
public class OperationLogAspect {
    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private TokenService tokenService;

    // 配置织入点
    @Pointcut("@annotation(com.github.lastsunday.moon.config.log.OperationLog)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            OperationLog operationLog = getAnnotationLog(joinPoint);
            if (operationLog == null) {
                return;
            }
            Date now = new Date();
            final OperationLogData operationLogData = new OperationLogData();
            // 获取当前请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // 获取当前的用户
            LoginUser loginUser = tokenService.getLoginUser(request);
            if (loginUser != null) {
                operationLogData.setOperatorId(loginUser.getId());
                operationLogData.setOperatorAccount(loginUser.getAccount());
            } else {
                // skip
            }
            operationLogData.setCreateTime(now);
            operationLogData.setFunctionModule(operationLog.functionModule());
            operationLogData.setOperation(operationLog.operation());
            operationLogData.setPath(request.getRequestURI());
            operationLogData.setIp(IpUtils.getIpAddr(request));
            String requestMethod = request.getMethod();
            if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
                String params = argsArrayToString(joinPoint.getArgs());
                operationLogData.setRequest(params);
            } else {
                Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                operationLogData.setRequest(paramsMap.toString());
            }
            if (e == null) {
                if (jsonResult == null) {
                    operationLogData.setResponse(null);
                } else {
                    operationLogData.setResponse(JSON.toJSONString(jsonResult));
                }
            } else {
                if (e instanceof CommonException) {
                    CommonException commonException = (CommonException) e;
                    operationLogData.setErrorCode(commonException.getErrorCode());
                    operationLogData.setErrorMessage(commonException.getErrorMessage());
                } else {
                    operationLogData.setErrorCode(CommonException.UNSPECIFIED_EXCEPTION);
                    operationLogData.setErrorMessage(e.getMessage());
                }
            }
            AsyncManager.me().execute(new TimerTask() {

                private final Logger log = LoggerFactory.getLogger(AsyncManager.class);

                @Override
                public void run() {
                    try {
                        OperationLogDO entity = new OperationLogDO();
                        BeanUtils.copyProperties(operationLogData, entity);
                        entity.setId(IdGenerator.genUniqueStringId());
                        entity.setFunctionModule(operationLogData.getFunctionModule().getCode());
                        entity.setOperation(operationLogData.getOperation().getCode());
                        SpringUtils.getBean(OperationLogMapper.class).insert(entity);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private OperationLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(OperationLog.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder sb = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    sb.append(jsonObj.toString());
                }
            }
        }
        return sb.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o == null || o instanceof MultipartFile || o instanceof HttpServletRequest
                || o instanceof HttpServletResponse;
    }
}
