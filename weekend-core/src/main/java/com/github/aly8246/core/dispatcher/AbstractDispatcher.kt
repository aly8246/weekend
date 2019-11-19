package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method
import java.lang.reflect.Parameter

abstract class AbstractDispatcher<T>(protected var proxy: Any, protected var method: Method, protected var args: Array<Any>?, target: Class<T>) : Dispatcher<T> {
    override fun execute(): T? {
        return run()
    }

    abstract fun run(): T?

    abstract fun template(baseCommand: String, param: MutableMap<Parameter, Any?>): String

}
