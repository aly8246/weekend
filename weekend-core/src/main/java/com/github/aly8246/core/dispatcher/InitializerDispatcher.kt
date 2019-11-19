package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.configuration.Configurations
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.driver.MongoStatement
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.template.RegexTemplate
import com.github.aly8246.core.util.PrintImpl
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import java.io.StringReader
import java.lang.Exception

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

abstract class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, var mongoConnection: MongoConnection, var target: Class<T>) : AbstractDispatcher<T>(proxy, method, args, target) {

    protected lateinit var baseCommand: String

    override fun run(): T? {
        //从方法的注解中获得sql/或者生成base sql
        baseCommand = this.resolverBaseCommand(method)

        //解析获得参数
        val param = resolverParam(method)

        //获得原始sql(模板替换完成)
        var originalCommand = template(baseCommand, param)

        //解析主键ID
        originalCommand = resolverPrimaryKey(originalCommand)

        //将解析好的sql传递给子类
        this.transmitOriginalCommand(originalCommand)

        //创建statement
        val statement = mongoConnection.createStatement()

        //选择执行的statement
        val executorResult = selectStatement(statement, originalCommand, param, statement)

        statement.close()

        return executorResult
    }

    abstract fun transmitOriginalCommand(originalCommand: String)

    //获得用户传入的command
    abstract fun resolverBaseCommand(method: Method): String

    //如果存在注解,则注解替换成_id
    abstract fun resolverPrimaryKey(originalCommand: String): String

    protected fun resolverParam(method: Method): MutableMap<Parameter, Any?> {
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

    override fun selectStatement(statement: Statement, command: String, param: MutableMap<Parameter, Any?>, statement1: Statement): T? {
        return when (val sqlStatement = CCJSqlParserManager().parse(StringReader(command.trim()))) {
            is Select -> {
                val executeQuery = statement.executeQuery(command) as MongoResultSet
                executeQuery.init(method, args, this.target)
                executeQuery.getObject() as T ?: return null
            }
            is Insert -> {
                val mongoStatement = statement as MongoStatement
                mongoStatement.param = param
                statement.addBatch(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            is Update -> {
                statement.executeUpdate(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            is Delete -> {
                statement.execute(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            else -> throw WeekendException("暂时不支持的语句 >> $sqlStatement")
        }
    }

    override fun template(baseCommand: String, param: MutableMap<Parameter, Any?>): String = RegexTemplate().completeCommand(baseCommand, param)
}
