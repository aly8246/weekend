package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.template.RegexTemplate
import java.lang.Exception

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

@Suppress("UNCHECKED_CAST")
abstract class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, var mongoConnection: MongoConnection) : AbstractDispatcher<T>(proxy, method, args) {
    private lateinit var command: Command

    final override fun run(): T? {
        try {
            command = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("方法上的 @com.github.aly8246.core.annotation.Command 注解不能为空")
        }

        val param = resolverParam(method)

        val original = template(this.command, param)

        val statement = mongoConnection.createStatement()

        val executeQuery = selectStatement(statement, original)
        executeQuery.init(command, method, args)

        return executeQuery.getObject() as T ?: return null
    }


    private fun resolverParam(method: Method): MutableMap<Parameter, Any?> {
        val paramMap: MutableMap<Parameter, Any?> = mutableMapOf()
        for (index in method.parameters.indices) {
            if (args!![index] != null)
                paramMap[method.parameters[index]] = args!![index]
        }

        paramMap.entries.forEach { e ->
            run {
                try {
                    println(e.key.name + ":" + e.value.toString())
                } catch (ex: Exception) {
                }
            }
        }

        return paramMap
    }

    abstract fun selectStatement(statement: Statement, command: String): MongoResultSet

    override fun template(command: Command, param: MutableMap<Parameter, Any?>): String = RegexTemplate().completeCommand(command,param)
}
