package com.template.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.template.annotation.Auditable;

/**
 * 審計切面：記錄敏感操作的審計日誌
 */
@Aspect
@Component
@Slf4j
public class AuditAspect {

    /**
     * 記錄操作前的審計信息
     */
    @Before("@annotation(auditable)")
    public void auditBefore(JoinPoint joinPoint, Auditable auditable) {
        String username = getCurrentUsername();
        String ipAddress = getClientIp();
        String action = getAction(joinPoint, auditable);
        String resourceType = auditable.resourceType();
        
        log.info("🔍 AUDIT START: User [{}] from IP [{}] is performing [{}] on [{}]",
            username, ipAddress, action, resourceType);
        
        if (auditable.includeArgs()) {
            log.info("🔍 AUDIT ARGS: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    /**
     * 記錄操作成功後的審計信息
     */
    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void auditAfterReturning(JoinPoint joinPoint, Auditable auditable, Object result) {
        String username = getCurrentUsername();
        String action = getAction(joinPoint, auditable);
        
        log.info("✅ AUDIT SUCCESS: User [{}] successfully completed [{}]", 
            username, action);
        
        if (auditable.includeResult() && result != null) {
            log.info("✅ AUDIT RESULT: {}", result);
        }
        
        // 這裡可以將審計日誌持久化到數據庫
        saveAuditLog(username, action, "SUCCESS", null);
    }

    /**
     * 記錄操作失敗的審計信息
     */
    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "exception")
    public void auditAfterThrowing(JoinPoint joinPoint, Auditable auditable, Exception exception) {
        String username = getCurrentUsername();
        String action = getAction(joinPoint, auditable);
        
        log.error("❌ AUDIT FAILURE: User [{}] failed to complete [{}]. Error: {}", 
            username, action, exception.getMessage());
        
        // 這裡可以將審計日誌持久化到數據庫
        saveAuditLog(username, action, "FAILURE", exception.getMessage());
    }

    /**
     * 獲取當前用戶名
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "Anonymous";
    }

    /**
     * 獲取客戶端 IP 地址
     */
    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return "Unknown";
    }

    /**
     * 獲取操作名稱
     */
    private String getAction(JoinPoint joinPoint, Auditable auditable) {
        if (!auditable.action().isEmpty()) {
            return auditable.action();
        }
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    /**
     * 保存審計日誌到數據庫（示例方法）
     */
    private void saveAuditLog(String username, String action, String status, String errorMessage) {
        // TODO: 實現審計日誌的持久化
        // 可以創建 AuditLog 實體並保存到數據庫
        log.debug("Saving audit log: User={}, Action={}, Status={}, Time={}", 
            username, action, status, LocalDateTime.now());
    }
}