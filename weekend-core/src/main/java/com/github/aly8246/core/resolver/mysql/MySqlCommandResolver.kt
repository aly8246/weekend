package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.resolver.*
import com.github.aly8246.core.util.PrintImpl
import java.util.stream.Collectors
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.util.TablesNamesFinder
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.SelectExpressionItem


open class MySqlCommandResolver : AbstractCommandResolver() {
    override fun sign(): String {
        return this.javaClass.simpleName
    }

    override fun validCommandSyntax(baseCommand: String): Boolean {
        try {
            PrintImpl().debug("语法检查:$baseCommand")
            CCJSqlParserUtil.parse(baseCommand)
        } catch (e: Exception) {
            return false
        }
        PrintImpl().debug("检查通过")
        return true
    }

    override fun resolverCommandOperation(baseCommand: String): Operation {

        val statement = CCJSqlParserUtil.parse(baseCommand)

        val operation = Operation()

        operation.tableName = TablesNamesFinder().getTableList(statement).stream().collect(Collectors.toList())[0]
        operation.baseCommand = baseCommand

        when (statement) {
            is Select -> {
                operation.operation = OperationEnum.SELECT
                operation.operationResolver = SelectCommandResolver()
                operation.field = (operation.operationResolver as SelectCommandResolver).resolverSelectItem(statement)
            }
            is Insert -> {
                operation.operation = OperationEnum.INSERT
                operation.operationResolver = InsertCommandResolver()
                operation.field = (operation.operationResolver as InsertCommandResolver).resolverSelectItem(statement)
            }
//            is Update -> operation.operation = OperationEnum.UPDATE
//            is Delete -> operation.operation = OperationEnum.DELETE
            else -> throw WeekendException("暂时无法解析的操作:$baseCommand")
        }
        return operation
    }


    override fun resolverCommandConditions(baseCommand: String, resolverCommandOperation: Operation): List<Condition> {
        return resolverCommandOperation.operationResolver?.resolverConditions(baseCommand)!!
    }

    override fun assemblingOperation(operation: Operation, conditionList: List<Condition>): Operation {
        operation.conditionList = conditionList
        return operation
    }
}