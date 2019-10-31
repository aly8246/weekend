package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method

open class DispatcherFactory<T> : DispatcherFactoryPolicy<T> {
    override fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?): Dispatcher<T> {
        val dispatcherInitializer = InitializerDispatcher<T>(proxy, method, args)
        val initializer = dispatcherInitializer.initializer()

        //结果集包含映射字段注解
        if (initializer.containerMapping())
            return CustomFieldDispatcher(proxy, method, args)

        //原始调度器
        return OriginalDispatcher(proxy, method, args)
    }
}