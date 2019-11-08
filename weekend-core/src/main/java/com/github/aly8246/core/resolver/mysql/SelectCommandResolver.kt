package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.query.enmu.OperationSignEnum
import com.github.aly8246.core.resolver.*
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.select.SelectExpressionItem
import net.sf.jsqlparser.util.TablesNamesFinder
import java.util.stream.Collectors


class SelectCommandResolver : OperationResolver {

    override fun resolverSelectItem(statement: Statement): MutableList<Field> {
        val tableName = TablesNamesFinder().getTableList(statement).stream().collect(Collectors.toList())[0]

        val select = statement as Select
        val selectItemList = (select.selectBody as PlainSelect).selectItems
        val fieldList: MutableList<Field> = mutableListOf()
        for (selectItem in selectItemList) {
            val field = Field()
            var selectExpressionItem: SelectExpressionItem?
            try {
                selectExpressionItem = selectItem as SelectExpressionItem
            } catch (ex: Exception) {
                continue
            }
            val column = selectExpressionItem.expression as Column

            //user_info
            field.tableName = tableName

            //u user_info
            if (column.table != null) field.tableAlias = column.table.name

            //.id
            field.field = column.columnName

            //as
            when {
                selectExpressionItem.alias != null -> field.ass = true
                else -> field.ass = false
            }

            //u
            when {
                selectExpressionItem.alias != null -> field.alias = selectExpressionItem.alias.name
            }

            fieldList.add(field)
        }

        return fieldList
    }

    override fun resolverConditions(baseCommand: String, resolverCommandOperation: Operation): List<Condition> {
        val selectCommandResolver = resolverCommandOperation.operationResolver as SelectCommandResolver
        val select = resolverCommandOperation.statement as Select

        println(select)

        val plain = select.selectBody as PlainSelect
        val conditionResolver = SourceConditionResolver()

        if (plain.where != null)
            conditionResolver.sourceBuildCondition(plain)


        //WHERE userMoney < 700 AND (age = 18 OR age = 22) AND 1 = 1 OR 2 = 2
        val conditions3 = Condition()
        conditions3.type = ConditionEnum.AND
        conditions3.fieldName = "age"
        conditions3.sign = OperationSignEnum.EQ
        conditions3.value = 18
        conditions3.group = "1"

        val conditions4 = Condition()
        conditions4.type = ConditionEnum.OR
        conditions4.fieldName = "age"
        conditions4.sign = OperationSignEnum.EQ
        conditions4.value = 22
        conditions4.group = "1"

        val conditions5 = Condition()
        conditions5.type = ConditionEnum.AND
        conditions5.fieldName = "userMoney"
        conditions5.sign = OperationSignEnum.LE
        conditions5.value = 700

        return listOf(conditions3, conditions4, conditions5)
    }


}