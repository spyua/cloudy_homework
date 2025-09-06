package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {
    
    @Builder.Default
    private Boolean success = true;
    
    private String message;
    
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public static <T> RestResponse<T> success() {
        return RestResponse.<T>builder().success(true).build();
    }
    
    public static <T> RestResponse<T> success(T data) {
        return RestResponse.<T>builder().success(true).data(data).build();
    }
    
    public static <T> RestResponse<T> success(String message, T data) {
        return RestResponse.<T>builder().success(true).message(message).data(data).build();
    }
    
    public static <T> RestResponse<T> error(String message) {
        return RestResponse.<T>builder().success(false).message(message).build();
    }
    
    public static <T> RestResponse<T> error(String message, T data) {
        return RestResponse.<T>builder().success(false).message(message).data(data).build();
    }
}