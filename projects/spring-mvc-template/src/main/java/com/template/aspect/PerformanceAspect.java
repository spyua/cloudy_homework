package com.template.aspect;

import com.template.annotation.TrackPerformance;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 性能監控切面：追蹤方法執行時間
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    /**
     * 監控標記了 @TrackPerformance 的方法
     */
    @Around("@annotation(trackPerformance)")
    public Object trackPerformance(ProceedingJoinPoint joinPoint, TrackPerformance trackPerformance) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        long startTime = System.currentTimeMillis();
        
        if (trackPerformance.logArgs()) {
            log.info("Executing {}.{} with args: {}", 
                className, methodName, Arrays.toString(joinPoint.getArgs()));
        }
        
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            
            if (elapsedTime > trackPerformance.warnThreshold()) {
                log.warn("⚠️ Performance Warning: {}.{} took {} ms (threshold: {} ms)",
                    className, methodName, elapsedTime, trackPerformance.warnThreshold());
            } else {
                log.info("✓ {}.{} completed in {} ms",
                    className, methodName, elapsedTime);
            }
            
            if (trackPerformance.logResult()) {
                log.info("Result: {}", result);
            }
            
            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("✗ {}.{} failed after {} ms - Error: {}",
                className, methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }
    
    /**
     * 監控標記了 @TrackPerformance 的類中的所有公共方法
     */
    @Around("@within(trackPerformance)")
    public Object trackClassPerformance(ProceedingJoinPoint joinPoint, TrackPerformance trackPerformance) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        
        // 如果方法本身也有 @TrackPerformance，則跳過（避免重複記錄）
        if (signature.getMethod().isAnnotationPresent(TrackPerformance.class)) {
            return joinPoint.proceed();
        }
        
        return trackPerformance(joinPoint, trackPerformance);
    }
}