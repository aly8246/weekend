package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import java.lang.reflect.Method

abstract class AbstractDispatcher<T>(protected var proxy: Any, protected var method: Method, protected var args: Array<Any>?) : Dispatcher<T> {
    override fun execute(): T? {
        return run()
    }

    abstract fun run(): T?
    //step1 初始化

    //step2 检查命令语法
    abstract fun syntaxCheck(command: Command): String

    //step3 将字符串拼接和时间模板方法替换成具体值
    abstract fun template(command: Command): String


    //step7 分组和统计等等方法的实现
    abstract fun handlePreview(t: T?): T?

    //step8 返回值的处理
    abstract fun handleResult(t: T?): T?
}
