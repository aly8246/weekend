package com.github.aly8246.core.query

import com.github.aly8246.core.handler.Conditions
import com.github.aly8246.core.handler.Operation
import com.github.aly8246.core.query.bean.ConditionGroup
import org.springframework.data.mongodb.core.query.Query

abstract class InitializerQueryBuilder : AbstractQueryBuilder(), ConditionGroupPolicy {

    override fun conditionGroupPolicy(operation: Operation): ConditionGroup {
        val conditionGroup = ConditionGroup()
        conditionGroup.commonConditions = commonConditions(operation)
        conditionGroup.groupConditions = groupConditions(operation)
        conditionGroup.sortConditions = sortConditions(operation)
        return conditionGroup
    }

    override fun addConditionGroup(conditionGroup: ConditionGroup): ConditionGroup {
        conditionGroup.groupByConditions = emptyList()
        return conditionGroup
    }

    override fun runBuild(conditionGroup: ConditionGroup): Query {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun dispatcherCondition(operation: Operation): ConditionGroup {
        return this.conditionGroupPolicy(operation)
    }

    override fun buildMongoQuery(operation: Operation): Query {
        println("执行构造初始化")
        return super.buildMongoQuery(operation)
    }


    override fun commonConditions(operation: Operation): List<Conditions> = SplitQueryCondition().analysisCommonConditions(operation)

    override fun groupConditions(operation: Operation): Map<String, List<Conditions>> = SplitQueryCondition().analysisGroupConditions(operation)

    override fun sortConditions(operation: Operation) = SplitQueryCondition().analysisSortConditions(operation)

    override fun groupByConditions(operation: Operation) = SplitQueryCondition().analysisGroupByConditions(operation)

    override fun getQuery(query: Query): Query {
        return query
    }

}