package com.github.aly8246.core.resolver

import net.sf.jsqlparser.statement.Statement

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
    var field: List<Field>? = null
    var tableName: String? = null
    var conditionList: List<Condition> = mutableListOf()
    var statement: Statement? = null
    var operationResolver: OperationResolver? = null
}
