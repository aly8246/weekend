package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.exception.WeekendException
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import java.io.StringReader
import java.lang.reflect.Method
import java.sql.Statement

@Suppress("UNCHECKED_CAST")
class OriginalDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection) : InitializerDispatcher<T>(proxy, method, args, mongoConnection) {
    override fun selectStatement(statement: Statement, command: String, commandAnnotation: Command): T? {
        return when (val sqlStatement = CCJSqlParserManager().parse(StringReader(command.trim()))) {
            is Select -> {
                val executeQuery = statement.executeQuery(command) as MongoResultSet
                executeQuery.init(commandAnnotation, method, args)
                executeQuery.getObject() as T ?: return null
            }
            is Insert -> {
                statement.addBatch(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            is Update -> throw WeekendException("还未实现更新语句")
            is Delete -> throw WeekendException("还未实现删除语句")
            else -> throw WeekendException("暂时不支持的语句 >> $sqlStatement")
        }
    }

}