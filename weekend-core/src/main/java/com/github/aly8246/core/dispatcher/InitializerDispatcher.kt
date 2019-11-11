package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.template.BaseTemplate
import com.github.aly8246.core.template.RegexTemplate

import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
open class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, var mongoConnection: MongoConnection) : AbstractDispatcher<T>(proxy, method, args) {
    private lateinit var command: Command

    final override fun run(): T? {
        try {
            command = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("方法上的 @com.github.aly8246.core.annotation.Command 注解不能为空")
        }

        val original = template(this.command)

        val statement = mongoConnection.createStatement()

        val executeQuery = statement.executeQuery(original) as MongoResultSet
        executeQuery.init(command, method)

        return executeQuery.getObject() as T ?: return null
    }


    override fun template(command: Command): String = RegexTemplate().completeCommand(command)

}
