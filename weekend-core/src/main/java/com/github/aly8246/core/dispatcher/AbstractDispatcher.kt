package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.handler.Operation
import org.springframework.data.mongodb.core.query.Query
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

    //step4 将命令处理成条件类
    abstract fun handleCommand(baseCommand: String): Operation

    //step5 解析条件类将条件类转换成Query类
    abstract fun buildQuery(handleCommand: Operation): Query

    //step6 根据情况选择不同的执行器
    abstract fun executor(operation: Operation, query: Query, method: Method): T?

    //step7 分组和统计等等方法的实现
    abstract fun handlePreview(t: T?): T?

    //step8 返回值的处理
    abstract fun handleResult(t: T?): T?
}
