package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:20
 * @description：
 * @version: ：V
 */
open abstract class AbstractDispatcher<T>(protected var proxy: Any, protected var method: Method, protected var args: Array<Any>?) : Dispatcher<T> {

    open override fun execute(): T {
        init()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //step0 进入动态代理

    abstract fun init()


    open abstract fun syntaxCheck()
    //step2 检查命令语法

    abstract fun template()
    //step3 将字符串拼接和时间模板方法替换成具体值

    abstract fun handleCommand()
    //step4 将命令处理成条件类

    abstract fun buildQuery()
    //step5 解析条件类将条件类转换成Query类

    abstract fun selectExecutor()
    //step6 根据情况选择不同的执行器

    abstract fun handleLater()
    //step7 分组和统计等等方法的实现

    abstract fun handleResult()
    //step8 返回值的处理
}
