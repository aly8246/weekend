package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 上午 10:50
 * @description：
 * @version:   ：V
 */
open class DispatcherFactory<T>(private var proxy: Any, private var method: Method, private var args: Array<Any>?) {


    fun dispatcher(): Dispatcher<T> {
        val dispatcherInitializer = InitializerDispatcher<T>(proxy, method, args)
        val initializer = dispatcherInitializer.initializer()

        //结果集包含映射字段注解
        if (initializer.containerMapping())
            return CustomFieldDispatcher(proxy, method, args)

        //原始调度器
        return OriginalDispatcher(proxy, method, args)
    }
}