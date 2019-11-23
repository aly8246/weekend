package com.github.aly8246.core.util

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/19 下午 12:52
 * @description：
 * @version: ：V
 */
class WordUtil {
    companion object {
        fun toLowerCaseFirstOne(str: String): String {
            if (str[0].isLowerCase()) {
                return str
            }
            return java.lang.StringBuilder().append(str[0].toLowerCase()).append(str.substring((1))).toString()
        }

        fun toUpperCaseFirstOne(str: String): String {
            if (str[0].isUpperCase()) {
                return str
            }
            return java.lang.StringBuilder().append(str[0].toUpperCase()).append(str.substring((1))).toString()
        }

        /**
         * 驼峰转下划线
         */
        fun underscoreName(str: String?): String {
            if (str == null || str.trim { it <= ' ' }.isEmpty()) {
                return ""
            }
            val len = str.length
            val sb = StringBuilder(len)
            sb.append(str.substring(0, 1).toLowerCase())
            for (i in 1 until len) {
                val c = str[i]
                if (Character.isUpperCase(c)) {
                    sb.append("_")
                    sb.append(Character.toLowerCase(c))
                } else {
                    sb.append(c)
                }
            }
            return sb.toString()
        }

        /**
         * 下划线转驼峰
         */
        fun camelName(name: String?): String {
            val result = java.lang.StringBuilder()
            if (name == null || name.isEmpty()) {
                return ""
            } else if (!name.contains("_")) {
                return name.substring(0, 1).toLowerCase() + name.substring(1)
            }
            val camels = name.split("_").toTypedArray()
            for (camel in camels) {
                if (camel.isEmpty()) {
                    continue
                }
                if (result.length == 0) {
                    result.append(camel.toLowerCase())
                } else {
                    result.append(camel.substring(0, 1).toUpperCase())
                    result.append(camel.substring(1).toLowerCase())
                }
            }
            return result.toString()
        }


    }


}
