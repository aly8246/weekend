package com.github.aly8246.weekend.spring.configuration.global

class WeekendGlobalProperties {
    /**
     * 不提示不存在的字段
     */
    var nonFieldRemind: Boolean? = null

    /**
     * 打印参数
     */
    var showParam: Boolean? = null

    /**
     * 打印原始命令
     */
    var showCommand: Boolean? = null

    /**
     * 打印执行条件
     */
    var showCondition: Boolean? = null

    /**
     * 显示返回结果
     */
    var showResult: Boolean? = null

    /**
     * 时间格式化
     */
    var dataFormat: String? = null
}