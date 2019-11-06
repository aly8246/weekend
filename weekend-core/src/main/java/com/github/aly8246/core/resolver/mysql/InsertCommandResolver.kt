package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.OperationResolver


class InsertCommandResolver : OperationResolver {
    override fun resolverConditions(baseCommand: String): List<Condition> {
        println("执行更新操作，但是还未实现")
        return emptyList()
    }
}