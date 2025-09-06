package com.template.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 性能追蹤註解
 * 標記需要監控執行時間的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackPerformance {
    
    /**
     * 警告閾值（毫秒）
     * 超過此時間將記錄警告日誌
     */
    long warnThreshold() default 1000;
    
    /**
     * 是否記錄參數
     */
    boolean logArgs() default false;
    
    /**
     * 是否記錄返回值
     */
    boolean logResult() default false;
}