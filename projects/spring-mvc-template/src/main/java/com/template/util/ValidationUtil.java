package com.template.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    
    private ValidationUtil() {
        // Utility class
    }
    
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return StringUtils.hasText(phone) && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidString(String str, int minLength, int maxLength) {
        if (!StringUtils.hasText(str)) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    public static boolean isPositive(Number number) {
        return number != null && number.doubleValue() > 0;
    }
    
    public static boolean isNonNegative(Number number) {
        return number != null && number.doubleValue() >= 0;
    }
}