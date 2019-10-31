package com.github.aly8246.core.query

import com.github.aly8246.core.handler.Operation
import org.springframework.data.mongodb.core.query.Query

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 03:11
 * @description：
 * @version:   ：V
 */
interface QueryBuilder {
    fun buildMongoQuery(operation: Operation): Query
}