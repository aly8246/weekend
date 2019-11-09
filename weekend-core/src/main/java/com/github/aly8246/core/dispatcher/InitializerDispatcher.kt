package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.configuration.ConfigurationUtil.Companion.configuration
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.template.BaseTemplate

import com.github.aly8246.core.util.ResultCase
import java.lang.reflect.Method
import java.util.regex.Pattern

@Suppress("UNCHECKED_CAST")
open class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?) : AbstractDispatcher<T>(proxy, method, args) {
    private lateinit var command: Command

    final override fun run(): T? {
        try {
            command = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("方法上的 @com.github.aly8246.core.annotation.Command 注解不能为空")
        }

        val original = template(this.command)

        val statement = configuration.connection.createStatement()

        val executeQuery = statement.executeQuery(original) as MongoResultSet
        executeQuery.init(command,method)
        //        val query = executeQuery.query
//        val list = mutableListOf(Document())
//        list.removeAt(0)
//        while (query.hasNext()) {
//            val next = query.next()
//            list.add(next)
//        }
//
//        println(list)
//        if (list.size == 1) MapUtil.mapToPojo(list[0], retClass.clazz());

//        return MapUtil.mapToList(list as List<MutableMap<Any?, Any?>>?, retClass.clazz()) as T
        return executeQuery.getObject() as T ?: return null
    }

    override fun syntaxCheck(command: Command): String = BaseTemplate().completeCommand(command)

    override fun template(command: Command): String = BaseTemplate().completeCommand(command)

}
