package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.resolver.*
import java.util.stream.Collectors
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.util.TablesNamesFinder


open class MySqlCommandResolver : AbstractCommandResolver() {
    override fun sign(): String {
        return this.javaClass.simpleName
    }

    override fun validCommandSyntax(baseCommand: String): Boolean {
        println("语法检查:$baseCommand")
        return true
    }

    override fun resolverCommandOperation(baseCommand: String): Operation {
        val statement = CCJSqlParserUtil.parse(baseCommand)
        val operation = Operation()
        operation.baseCommand = baseCommand
        operation.operation = commandOperation(statement)
        operation.field = "*"
        operation.tableName = TablesNamesFinder().getTableList(statement).stream().collect(Collectors.joining())

        return operation
    }

    private fun commandOperation(statement: Statement): OperationEnum? {
        return when (statement) {
            is Select -> OperationEnum.SELECT
            is Insert -> OperationEnum.INSERT
            is Update -> OperationEnum.UPDATE
            is Delete -> OperationEnum.DELETE
            else -> OperationEnum.OTHER
        }
    }

    override fun resolverCommandConditions(baseCommand: String, resolverCommandOperation: Operation): List<Condition> {
        val selector = when (resolverCommandOperation.operation) {
            OperationEnum.SELECT -> SelectCommandResolver()
            OperationEnum.INSERT -> InsertCommandResolver()
//            OperationEnum.UPDATE -> println("执行更新操作")
//            OperationEnum.DELETE -> println("执行删除操作")
//            OperationEnum.OTHER -> println("执行其他操作")
            else -> return emptyList()
        }

        return selector.resolverConditions(baseCommand)
    }

    override fun assemblingOperation(operation: Operation, conditionList: List<Condition>): Operation {
        operation.conditionList = conditionList
        return operation
    }
}