package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.statement.insert.Insert
import java.io.StringReader
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

@Suppress("UNCHECKED_CAST")
open class OriginalDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>) : InitializerDispatcher<T>(proxy, method, args, mongoConnection, target) {
    protected lateinit var commandAnnotation: Command

    protected lateinit var originalCommand: String

    override fun resolverBaseCommand(method: Method): String {
        try {
            commandAnnotation = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
        }

        return commandAnnotation.value.joinToString("")
    }

    override fun transmitOriginalCommand(originalCommand: String) {
        this.originalCommand = originalCommand
    }

    override fun resolverPrimaryKey(originalCommand: String): String {
        val primaryKey = this.commandAnnotation.primaryKey

        when (val statement = CCJSqlParserManager().parse(StringReader(originalCommand.trim()))) {
            is Insert -> {
                val columns = statement.columns
                for (column in columns) {
                    if (column.toString().replace("`", "") == primaryKey || column.toString().replace("`", "") == "id") {
                        column.columnName = "_id"
                        return statement.toString()
                    }
                }
                //如果没有显式指定primaryKey的值或者primaryKey不存在列中
                //列中没有默认id名称id 或者 _id
                when {
                    columns.stream().anyMatch { e -> e.columnName.replace("`", "") == "id" || e.columnName.replace("`", "") == "_id" } -> return statement.toString()
                }
            }
            else -> return originalCommand
        }
        throw WeekendException("不明确的主键类型,既不是id或者_id,又没有指定command注解的primaryKey")
    }

    //提供给子类重写,使子类可以重写template来选择模板引擎
    override fun template(baseCommand: String, param: MutableMap<String, Any?>): String {
        return super.template(baseCommand, param)
    }

    override fun selectStatement(statement: Statement, command: String, param: MutableMap<String, Any?>, statement1: Statement): T? {
        return super.selectStatement(statement, command, param, statement)
    }
}