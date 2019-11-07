package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.query.enmu.OperationEnum
import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.ConditionEnum
import com.github.aly8246.core.resolver.Field
import com.github.aly8246.core.resolver.OperationResolver
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

    override fun resolverConditions(baseCommand: String): List<Condition> {
        val conditions3 = Condition()
        conditions3.type = ConditionEnum.OR
        conditions3.fieldName = "age"
        conditions3.sign = OperationEnum.EQ
        conditions3.value = 18
        conditions3.group = "1"
        val conditions4 = Condition()
        conditions4.type = ConditionEnum.OR
        conditions4.fieldName = "age"
        conditions4.sign = OperationEnum.EQ
        conditions4.value = 22
        conditions4.group = "1"

        val conditions5 = Condition()
        conditions5.type = ConditionEnum.AND
        conditions5.fieldName = "userMoney"
        conditions5.sign = OperationEnum.LE
        conditions5.value = 700
        //conditions5.setGroup("1");

        //年龄是18或者22
        //AND ( a.login_name LIKE '%${keyword}%' OR a.real_name LIKE '%${keyword}%')
        //and (age like 18 or age like 22)
        //一个联合or需要解析成or查询组,但是query里需要
        /**
        if (pageReqParams.getStartTime() != null && pageReqParams.getEndTime() != null) {
        ----Criteria criteria = Criteria.where("_id").ne(0L);
        ----query.addCriteria(criteria.andOperator(
        ------------Criteria.where("collectionTime").gte(pageReqParams.getStartTime()),
        ------------Criteria.where("collectionTime").lte(pageReqParams.getEndTime()))
        ----);
        }
         */

        return listOf(conditions3, conditions4, conditions5)
    }


//    override fun resolverCommandConditions(baseCommand: String): List<Condition> {
//        val conditions3 = Condition()
//        conditions3.type = ConditionEnum.OR
//        conditions3.fieldName = "age"
//        conditions3.sign = OperationEnum.EQ
//        conditions3.value = 18
//        conditions3.group = "1"
//        val conditions4 = Condition()
//        conditions4.type = ConditionEnum.OR
//        conditions4.fieldName = "age"
//        conditions4.sign = OperationEnum.EQ
//        conditions4.value = 22
//        conditions4.group = "1"
//
//        val conditions5 = Condition()
//        conditions5.type = ConditionEnum.AND
//        conditions5.fieldName = "userMoney"
//        conditions5.sign = OperationEnum.LE
//        conditions5.value = 700
//        //conditions5.setGroup("1");
//
//        //年龄是18或者22
//        //AND ( a.login_name LIKE '%${keyword}%' OR a.real_name LIKE '%${keyword}%')
//        //and (age like 18 or age like 22)
//        //一个联合or需要解析成or查询组,但是query里需要
//        /**
//        if (pageReqParams.getStartTime() != null && pageReqParams.getEndTime() != null) {
//        ----Criteria criteria = Criteria.where("_id").ne(0L);
//        ----query.addCriteria(criteria.andOperator(
//        ------------Criteria.where("collectionTime").gte(pageReqParams.getStartTime()),
//        ------------Criteria.where("collectionTime").lte(pageReqParams.getEndTime()))
//        ----);
//        }
//         */
//
//        return listOf(conditions3, conditions4, conditions5)
//    }


}