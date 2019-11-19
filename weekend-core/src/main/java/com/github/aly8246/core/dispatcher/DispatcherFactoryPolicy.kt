package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method

interface DispatcherFactoryPolicy<T> {
    fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?, target: Class<T>): Dispatcher<T>
}