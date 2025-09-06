package com.template.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * 日誌切面：統一處理 API 請求日誌記錄
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * 定義切點：所有 Controller 層的公共方法
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    /**
     * 定義切點：所有 Service 層的公共方法
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
    }

    /**
     * Controller 層方法執行前記錄請求信息
     */
    @Before("controllerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            log.info("========================================");
            log.info("Request URL    : {} {}", request.getMethod(), request.getRequestURL());
            log.info("Client IP      : {}", getClientIp(request));
            log.info("Class Method   : {}.{}", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
            log.info("Request Args   : {}", Arrays.toString(joinPoint.getArgs()));
            log.info("========================================");
        }
    }

    /**
     * Controller 層方法執行後記錄響應信息
     */
    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void logAfterReturning(Object result) {
        log.info("Response       : {}", result);
        log.info("========================================");
    }

    /**
     * Service 層方法性能監控
     */
    @Around("servicePointcut()")
    public Object logServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            
            if (elapsedTime > 1000) {
                log.warn("Slow Service: {}.{} executed in {} ms", 
                    className, methodName, elapsedTime);
            } else {
                log.debug("Service: {}.{} executed in {} ms", 
                    className, methodName, elapsedTime);
            }
            
            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("Service: {}.{} failed after {} ms - Error: {}", 
                className, methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }

    /**
     * 異常發生時記錄
     */
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in {}.{} with cause = {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            exception.getMessage());
    }

    /**
     * 獲取客戶端真實 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}