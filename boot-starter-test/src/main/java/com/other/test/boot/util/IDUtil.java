package com.other.test.boot.util;

import java.util.UUID;

public class IDUtil {
    public static String createID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
