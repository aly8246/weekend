package com.github.aly8246.core.query.enmu

import com.github.aly8246.core.exception.WeekendException

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:42
 * @description：
 * @version: ：V
 */
enum class OperationSignEnum(sign: String) {
    // 大于
    GT(">"),
    // 小于
    LT("<"),
    // 等于
    EQ("="),
    // 大于等于
    GE(">="),
    // 小于等于
    LE("<="),
    // 不等于
    NQ("!="),
    // 在...中
    IN("in");

    // 以..开头
    // 以..结尾
    // 包含

    companion object {
        fun selectOperationSignEnum(sign: String): OperationSignEnum {
            if ("<" == sign) return OperationSignEnum.GT
            if (">" == sign) return OperationSignEnum.LT
            if ("=" == sign) return OperationSignEnum.EQ
            if (">=" == sign) return OperationSignEnum.GE
            if ("<=" == sign) return OperationSignEnum.LE
            if ("!=" == sign) return OperationSignEnum.NQ
            if ("in" == sign) return OperationSignEnum.IN
            throw WeekendException("未知的操作符:$sign")
        }
    }
}
