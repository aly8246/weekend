package com.github.aly8246.core.resolver

import com.github.aly8246.core.handler.Conditions
import com.github.aly8246.core.handler.Operation
import com.github.aly8246.core.query.enmu.OperationEnum
import java.util.*
import java.util.stream.Collectors

class MySqlCommandResolver : AbstractCommandResolver() {
    override fun sign(): String {
        return this.javaClass.simpleName
    }

    override fun validCommandSyntax(baseCommand: String): Boolean {
        println("语法检查:$baseCommand")
        return true
    }

    override fun resolverCommandOperation(baseCommand: String): Operation {
        val operation = Operation()
        operation.baseCommand = baseCommand
        operation.operation = com.github.aly8246.core.handler.OperationEnum.SELECT
        operation.field = "*"
        operation.tableName = Arrays
                .stream(baseCommand.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .collect(Collectors.toList())[3]

        return operation
    }

    override fun resolverCommandConditions(baseCommand: String): List<Conditions> {
        val conditions3 = Conditions()
        conditions3.type = com.github.aly8246.core.handler.QueryEnum.OR
        conditions3.fieldName = "age"
        conditions3.sign = OperationEnum.EQ
        conditions3.value = 18
        conditions3.group = "1"
        val conditions4 = Conditions()
        conditions4.type = com.github.aly8246.core.handler.QueryEnum.OR
        conditions4.fieldName = "age"
        conditions4.sign = OperationEnum.EQ
        conditions4.value = 22
        conditions4.group = "1"

        val conditions5 = Conditions()
        conditions5.type = com.github.aly8246.core.handler.QueryEnum.AND
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

    override fun assemblingOperation(operation: Operation, conditionList: List<Conditions>): Operation {
        operation.conditionsList = conditionList
        return operation
    }
}