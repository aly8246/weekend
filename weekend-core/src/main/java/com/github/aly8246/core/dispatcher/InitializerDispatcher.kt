package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.util.ResultCase
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.regex.Pattern

open class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?) : AbstractDispatcher<T>(proxy, method, args) {
    protected lateinit var retClass: RetClass
    protected lateinit var command: Command

    override fun init() {
        try {
            command = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("方法上的 @com.github.aly8246.core.annotation.Command 注解不能为空")
        }

        //返回类型
        val returnType = method.returnType

        //推断返回类型
        val eductionResult = ResultCase.getInstance(returnType)
        //实际返回类型>如果返回类型为集合,则应该取集合里的类型
        var realityResult: Any? = null

        //默认返回类型
        if (command.returnType != Collection::class.java) {
            try {
                realityResult = returnType.newInstance()
            } catch (e: java.lang.Exception) {
                realityResult = ArrayList<Any>()
            }
        } else {
            //简洁的名称
            val canonicalName = returnType.canonicalName
            if (canonicalName != "void")
                realityResult = try {
                    //尝试使用生产实例
                    Class.forName(canonicalName).newInstance()
                } catch (e: Exception) {
                    //如果是集合接口或者为空则无法成功实例化
                    Class.forName(regxListParamClass(this.method.toGenericString())).newInstance()
                }
        }
        this.retClass = RetClass(eductionResult, realityResult)
    }

    override fun syntaxCheck() {

    }

    override fun template() {

    }

    override fun handleCommand() {

    }

    override fun buildQuery() {

    }

    override fun selectExecutor() {

    }

    override fun handleLater() {

    }

    override fun handleResult() {

    }

    protected fun regxListParamClass(source: String): String {
        val matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source)
        while (matcher.find()) return matcher.group()
        throw WeekendException("异常regxListParamClass:$source")
    }
}
