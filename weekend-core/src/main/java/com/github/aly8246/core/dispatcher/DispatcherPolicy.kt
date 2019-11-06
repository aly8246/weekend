package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.resolver.Operation
import org.springframework.data.mongodb.core.query.Query
import java.lang.reflect.Method

interface DispatcherPolicy<T> {
    fun executorPolicy(operation: Operation, query: Query, method: Method): T?
}