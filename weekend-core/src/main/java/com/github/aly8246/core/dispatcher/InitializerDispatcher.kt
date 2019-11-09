package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.template.BaseTemplate
import com.github.aly8246.core.util.MapUtil

import com.github.aly8246.core.util.ResultCase
import org.bson.Document
import java.lang.reflect.Method
import java.sql.Connection
import java.sql.Statement
import java.util.*
import java.util.regex.Pattern

open class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, var statement: Statement, connection: Connection) : AbstractDispatcher<T>(proxy, method, args), DispatcherInitializer {
    protected lateinit var retClass: RetClass
    protected lateinit var command: Command

    final override fun run(): T? {
        this.retClass = initializer()

        val original = template(this.command)

        val executeQuery = statement.executeQuery(original) as MongoResultSet
        val query = executeQuery.query
        var list = mutableListOf(Document())
        list.removeAt(0)
        while (query.hasNext()) {
            val next = query.next()
            list.add(next)
        }

        println(list)
        if (list.size == 1) MapUtil.mapToPojo(list[0], retClass.clazz());

        return MapUtil.mapToList(list as List<MutableMap<Any?, Any?>>?, retClass.clazz()) as T
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

        when {
            command.returnType.java != Collection::class.java -> realityResult = try {
                returnType.newInstance()
            } catch (e: java.lang.Exception) {
                ArrayList<Any>()
            }
            else -> {
                val canonicalName = returnType.canonicalName
                when {
                    canonicalName != "void" -> realityResult = try {
                        Class.forName(canonicalName).newInstance()
                    } catch (e: Exception) {
                        Class.forName(regxListParamClass(this.method.toGenericString())).newInstance()
                    }
                }
            }
        }
        this.retClass = RetClass(eductionResult, realityResult)
        return retClass
    }

    override fun syntaxCheck(command: Command): String = BaseTemplate().completeCommand(command)

    override fun template(command: Command): String = BaseTemplate().completeCommand(command)


    override fun handlePreview(t: T?): T? {
        return t
    }

    override fun handleResult(t: T?): T? {
        return t
    }

    private fun regxListParamClass(source: String): String {
        val matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source)
        while (matcher.find()) return matcher.group()
        throw WeekendException("异常regxListParamClass:$source")
    }

}
