package com.github.aly8246.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReturnTypeUtil {
    public static String regxListParamClass(String source) {
        Matcher matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source);
        for (; matcher.find(); ) {
            return matcher.group();
        }
        throw new RuntimeException("异常regxListParamClass");
    }

}
