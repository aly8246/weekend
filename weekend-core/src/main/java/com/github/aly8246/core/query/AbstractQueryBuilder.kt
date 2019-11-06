package com.github.aly8246.core.query

import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.Operation
import com.github.aly8246.core.query.bean.ConditionGroup
import org.springframework.data.mongodb.core.query.Query

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 03:17
 * @description：
 * @version:   ：V
 */
abstract class AbstractQueryBuilder : QueryBuilderInterface {
    override fun buildMongoQuery(operation: Operation): Query {

        //将条件组合
        val conditionGroup = dispatcherCondition(operation)

        //将条件拆分执行构造
        val runBuild = runBuild(conditionGroup)

        return getQuery(runBuild)
    }

    //处理解析流程
    abstract fun dispatcherCondition(operation: Operation): ConditionGroup

    //执行构建
    abstract fun runBuild(conditionGroup: ConditionGroup): Query

    //step. 普通and和or
    abstract fun commonConditions(operation: Operation): List<Condition>

    //step. 组合and和or
    abstract fun groupConditions(operation: Operation): Map<String, List<Condition>>

    //step. sort
    abstract fun sortConditions(operation: Operation): List<Condition>

    //step. groupBy
    abstract fun groupByConditions(operation: Operation): List<Condition>


    abstract fun getQuery(query: Query): Query


}


