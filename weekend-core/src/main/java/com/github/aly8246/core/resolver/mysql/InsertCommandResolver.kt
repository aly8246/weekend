package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.Field
import com.github.aly8246.core.resolver.OperationResolver
import net.sf.jsqlparser.statement.Statement


class InsertCommandResolver : OperationResolver {
    override fun resolverSelectItem(statement: Statement): MutableList<Field> {
        println("执行更新操作，但是还未实现")
        throw WeekendException("执行更新操作，但是还未实现")
    }

    override fun resolverConditions(baseCommand: String): List<Condition> {
        println("执行更新操作，但是还未实现")
        return emptyList()
    }
}