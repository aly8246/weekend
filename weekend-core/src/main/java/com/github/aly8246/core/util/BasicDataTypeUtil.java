package com.github.aly8246.core.util;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class BasicDataTypeUtil {
    private static List<String> basicTypeList = new ArrayList<>();

    static {
        basicTypeList.add("java.lang.String");
        basicTypeList.add("java.lang.Integer");
        basicTypeList.add("java.lang.Double");
        basicTypeList.add("java.lang.Long");
        basicTypeList.add("java.lang.Short");
        basicTypeList.add("java.lang.Float");
        basicTypeList.add("java.util.Date");
        basicTypeList.add("int");
        basicTypeList.add("double");
        basicTypeList.add("long");
        basicTypeList.add("short");
        basicTypeList.add("float");
    }

    public static boolean isBasicDataType(Parameter parameter) {
        return basicTypeList.stream().anyMatch(parameter.getParameterizedType().getTypeName()::equals);
    }
}
