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

        fun camelToUnderline(str: String?): String {
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

    }


}
