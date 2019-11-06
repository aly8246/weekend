package com.github.aly8246.core.query

import com.github.aly8246.core.query.bean.ConditionGroup
import com.github.aly8246.core.query.queryBuilder.AndBuilder
import com.github.aly8246.core.query.queryBuilder.OrBuilder
import org.springframework.data.mongodb.core.query.Query


/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 06:56
 * @description：
 * @version:   ：V
 */
class AssemblerQuery : InitializerQueryBuilder() {

    override fun runBuild(conditionGroup: ConditionGroup): Query {
        var query = Query()
        val commonConditions = conditionGroup.commonConditions
        //根据分组and和or条件来构造query
        when {
            commonConditions.isNotEmpty() -> {
                //根据普通and和or条件来构造query
                val andBuilder = AndBuilder(query)
                query = andBuilder.buildQuery(commonConditions)
            }
        }

        val groupConditions = conditionGroup.groupCondition
        if (groupConditions.isNotEmpty()) {
            val orIterator = groupConditions.entries.iterator()
            while (orIterator.hasNext()) {
                val next = orIterator.next()
                val value = next.value
                val OrBuilder = OrBuilder(query)
                query = OrBuilder.buildQuery(value)
            }
            //根据分组and和or条件来构造query
        }
        return query
    }
}