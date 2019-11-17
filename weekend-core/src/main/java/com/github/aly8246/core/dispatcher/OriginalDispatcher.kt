package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
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
class OriginalDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection) : InitializerDispatcher<T>(proxy, method, args, mongoConnection) {
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

    override fun selectStatement(statement: Statement, command: String, commandAnnotation: Command, param: MutableMap<Parameter, Any?>): T? {
        return when (val sqlStatement = CCJSqlParserManager().parse(StringReader(command.trim()))) {
            is Select -> {
                val executeQuery = statement.executeQuery(command) as MongoResultSet
                executeQuery.init(commandAnnotation, method, args)
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