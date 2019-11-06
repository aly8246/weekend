package com.github.aly8246.core.query.queryBuilder

import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.query.queryBuilder.basic.OperationSignCriteriaBuilder
import org.springframework.data.mongodb.core.query.Query


/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
class AndBuilder(private val query: Query) : QueryBuilder {

    override fun buildQuery(conditionList: List<Condition>): Query {
        for (conditions in conditionList) {
            val fieldName = conditions.fieldName
            val value = conditions.value
            val criteriaBuilder = OperationSignCriteriaBuilder()

            val criteria = criteriaBuilder.build(fieldName, value, conditions.sign)
            query.addCriteria(criteria)

        }
        return query
    }
}
