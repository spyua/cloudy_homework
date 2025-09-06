package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 審計註解
 * 標記需要記錄審計日誌的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    
    /**
     * 操作類型
     */
    String action() default "";
    
    /**
     * 資源類型
     */
    String resourceType() default "";
    
    /**
     * 是否記錄請求參數
     */
    boolean includeArgs() default true;
    
    /**
     * 是否記錄返回結果
     */
    boolean includeResult() default false;
}