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
        val select = resolverCommandOperation.statement as Select

        val plain = select.selectBody as PlainSelect
        val conditionResolver = SourceConditionResolver()

        if (plain.where != null)
            return SourceConditionResolver().sourceBuildCondition(plain)
        return emptyList()
    }


}