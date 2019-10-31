package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.handler.Operation
import org.springframework.data.mongodb.core.query.Query
import java.lang.reflect.Method
import java.util.*

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 上午 10:38
 * @description：
 * @version:   ：V
 */
interface DispatcherPolicy<T> {
    fun executorPolicy(operation: Operation, query: Query, method: Method): T?

}