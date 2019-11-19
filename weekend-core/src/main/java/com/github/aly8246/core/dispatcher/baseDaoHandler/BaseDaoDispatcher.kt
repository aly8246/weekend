package com.github.aly8246.core.dispatcher.baseDaoHandler


import com.github.aly8246.core.dispatcher.InitializerDispatcher
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.driver.MongoStatement
import com.github.aly8246.core.exception.WeekendException
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import java.io.StringReader
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

@Suppress("UNCHECKED_CAST")
class BaseDaoDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, var target: Class<T>) : InitializerDispatcher<T>(proxy, method, args, mongoConnection, target) {
    protected lateinit var originalCommand: String
    override fun resolverBaseCommand(method: Method): String {
        //根据方法拼接sql
        val contextFactory = BaseDaoContextFactory<T>()
        val strategy = contextFactory.produceStrategy(null, method.name)
        return strategy.create(proxy, method, args, mongoConnection, target)
    }

    override fun transmitOriginalCommand(originalCommand: String) {
        this.originalCommand = originalCommand
    }

    override fun resolverPrimaryKey(originalCommand: String): String {
        return originalCommand
    }

    override fun selectStatement(statement: Statement, command: String, param: MutableMap<Parameter, Any?>): T? {
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
}