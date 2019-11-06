package com.github.aly8246.core.resolver

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:26
 * @description：
 * @version: ：V
 */
class Operation {
    var baseCommand: String? = null
    var operation: OperationEnum? = null
    var field: String? = null
    var tableName: String? = null
    var conditionList: List<Condition> = mutableListOf()
}
