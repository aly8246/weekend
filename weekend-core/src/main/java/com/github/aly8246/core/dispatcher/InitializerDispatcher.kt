package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.configuration.Configurations
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.template.RegexTemplate
import com.github.aly8246.core.util.PrintImpl
import java.lang.Exception

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

abstract class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, var mongoConnection: MongoConnection) : AbstractDispatcher<T>(proxy, method, args) {
    private lateinit var commandAnnotation: Command

    final override fun run(): T? {
        try {
            commandAnnotation = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("方法上的 @com.github.aly8246.core.annotation.Command 注解不能为空")
        }

        val param = resolverParam(method)

        val originalCommand = template(this.commandAnnotation, param)

        val statement = mongoConnection.createStatement()

        return selectStatement(statement, originalCommand,this.commandAnnotation)
    }


    private fun resolverParam(method: Method): MutableMap<Parameter, Any?> {
        val paramMap: MutableMap<Parameter, Any?> = mutableMapOf()
        for (index in method.parameters.indices) {
            if (args!![index] != null)
                paramMap[method.parameters[index]] = args!![index]
        }
        if (Configurations.configuration.showParam!!)
            paramMap.entries.forEach { e ->
                run {
                    try {
                        PrintImpl().debug("param >>   " + e.key.name + ":" + e.value.toString())
                    } catch (ex: Exception) {
                    }
                }
            }

        return paramMap
    }

    abstract fun selectStatement(statement: Statement, command: String, commandAnnotation: Command): T?

    override fun template(command: Command, param: MutableMap<Parameter, Any?>): String = RegexTemplate().completeCommand(command, param)
}
