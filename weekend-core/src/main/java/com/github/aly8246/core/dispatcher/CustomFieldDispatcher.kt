package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.dispatcher.RetClassEnum.*
import com.github.aly8246.core.exec.CustomExecutor
import com.github.aly8246.core.exec.SelectExecutor
import com.github.aly8246.core.handler.Operation
import com.github.aly8246.core.handler.OperationEnum
import com.github.aly8246.core.handler.OperationEnum.*
import org.springframework.data.mongodb.core.query.Query
import java.lang.reflect.Method


@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA", "UNCHECKED_CAST")
class CustomFieldDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?) : InitializerDispatcher<T>(proxy, method, args) {
    override fun executorPolicy(operation: Operation, query: Query, method: Method): T? {
        when (operation.operation) {
            INSERT,

            DELETE,

            UPDATE,

            SELECT -> {
                run {
                    when (this.retClass.classType()) {
                        OBJECT -> {
                            println("查询单个")
                            return CustomExecutor().select(query, this.retClass.clazz(), operation.tableName, method) as T
                        }
                        COLLECTION -> return CustomExecutor().selectList(query, this.retClass.clazz(), operation.tableName, method) as T
                        PAGE -> {
                            println("可能需要分页，但是现在还没有处理")
                            println("警告，查询结果为空，不做任何处理，也不执行查询")
                        }
                        NULL -> println("警告，查询结果为空，不做任何处理，也不执行查询")
                    }
                }
                throw RuntimeException("暂时无法处理")
            }
            OTHER -> throw RuntimeException("暂时无法处理")
            else -> throw RuntimeException("暂时无法处理")
        }
    }

    override fun handlePreview(t: T?): T? {
        println("后续处理还未实现")
        return super.handlePreview(t)
    }

    override fun handleResult(t: T?): T? {
        println("结果集处理还未实现")
        return super.handleResult(t)
    }
}