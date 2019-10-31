package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.handler.Condition
import com.github.aly8246.core.handler.Operation
import com.github.aly8246.core.handler.SqlConditionHandler
import com.github.aly8246.core.query.QueryRunner
import com.github.aly8246.core.template.BaseTemplate
import com.github.aly8246.core.util.ResultCase
import org.springframework.data.mongodb.core.query.Query
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.regex.Pattern

open class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?) : AbstractDispatcher<T>(proxy, method, args), DispatcherPolicy<T>, DispatcherInitializer {

    override fun executorPolicy(operation: Operation, query: Query, method: Method): T? {
        throw WeekendException("未实现executorPolicy")
    }

    protected lateinit var retClass: RetClass
    protected lateinit var command: Command

    final override fun run(): T? {
        this.retClass = initializer()

        val original = template(this.command)

        val operation = handleCommand(original);

        val query = buildQuery(operation)

        var executor = executor(operation, query, method)

        //执行处理结果
        executor = handlePreview(executor)
        executor = handleResult(executor)
        return executor
    }

    override fun initializer(): RetClass {
        try {
            command = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("方法上的 @com.github.aly8246.core.annotation.Command 注解不能为空")
        }

        val returnType = method.returnType

        //推断返回类型
        val eductionResult = ResultCase.getInstance(returnType)
        //实际返回类型>如果返回类型为集合,则应该取集合里的类型
        var realityResult: Any? = null

        if (command.returnType.java != Collection::class.java) {
            try {
                realityResult = returnType.newInstance()
            } catch (e: java.lang.Exception) {
                realityResult = ArrayList<Any>()
            }
        } else {
            val canonicalName = returnType.canonicalName
            if (canonicalName != "void")
                realityResult = try {
                    Class.forName(canonicalName).newInstance()
                } catch (e: Exception) {
                    Class.forName(regxListParamClass(this.method.toGenericString())).newInstance()
                }
        }
        this.retClass = RetClass(eductionResult, realityResult)
        return retClass
    }

    override fun syntaxCheck(command: Command): String = BaseTemplate().completeCommand(command)

    override fun template(command: Command): String = BaseTemplate().completeCommand(command)

    override fun handleCommand(baseCommand: String): Operation = buildCondition(this.command, baseCommand)

    override fun buildQuery(handleCommand: Operation): Query = QueryRunner().run(handleCommand.conditionsList)

    override fun executor(operation: Operation, query: Query, method: Method): T? {
        return this.executorPolicy(operation, query, this.method)
    }

    override fun handlePreview(t: T?): T? {
        return t
    }

    override fun handleResult(t: T?): T? {
        return t
    }

    protected fun regxListParamClass(source: String): String {
        val matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source)
        while (matcher.find()) return matcher.group()
        throw WeekendException("异常regxListParamClass:$source")
    }

    private fun getConditionHandler(command: Command): Condition {
        val handler = command.handler.java
        try {
            return handler.newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return SqlConditionHandler()
    }

    protected fun buildCondition(command: Command, baseCommand: String): Operation {
        return this.getConditionHandler(command).run(baseCommand)
    }
}
