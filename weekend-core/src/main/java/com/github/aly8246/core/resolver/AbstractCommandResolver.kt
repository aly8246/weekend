package com.github.aly8246.core.resolver

import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.handler.Conditions
import com.github.aly8246.core.handler.Operation

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/06 下午 03:09
 * @description：
 * @version:   ：V
 */
abstract class AbstractCommandResolver : CommandResolver {
    override fun run(baseCommand: String): Operation {
        val validCommandSyntax = this.validCommandSyntax(baseCommand)
        if (!validCommandSyntax) throw WeekendException("命令语法检查不通过,此消息由${sign()}传递")

        val resolverCommandOperation = this.resolverCommandOperation(baseCommand)
        val resolverCommandConditions = this.resolverCommandConditions(baseCommand)

        return assemblingOperation(resolverCommandOperation, resolverCommandConditions)
    }

    abstract fun sign(): String

    //验证语法
    abstract fun validCommandSyntax(baseCommand: String): Boolean

    //解析命令主体
    abstract fun resolverCommandOperation(baseCommand: String): Operation

    //解析命令条件
    abstract fun resolverCommandConditions(baseCommand: String): List<Conditions>

    //完结组装
    abstract fun assemblingOperation(operation: Operation, conditionList: List<Conditions>): Operation
}