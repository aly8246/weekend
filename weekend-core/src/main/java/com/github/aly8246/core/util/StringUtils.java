package com.github.aly8246.core.util;

public class StringUtils {
    public static Boolean isNotEmpty(Object str) {
        return str != null && !str.equals("");
    }

    public static Boolean isEmpty(Object str) {
        return str == null || str.equals("");
    }
}
