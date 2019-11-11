package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import java.lang.reflect.Method

abstract class AbstractDispatcher<T>(protected var proxy: Any, protected var method: Method, protected var args: Array<Any>?) : Dispatcher<T> {
    override fun execute(): T? {
        return run()
    }

    abstract fun run(): T?


    abstract fun template(command: Command): String

}
