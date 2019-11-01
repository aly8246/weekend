package com.github.aly8246.core.query.queryBuilder

import com.github.aly8246.core.handler.Conditions
import com.github.aly8246.core.query.queryBuilder.basic.CriteriaBuilder
import com.github.aly8246.core.query.queryBuilder.basic.OperationSignCriteriaBuilder
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

import java.util.Arrays


/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
class AndBuilder(private val query: Query) : QueryBuilder {

    override fun buildQuery(conditionsList: List<Conditions>): Query {
        for (conditions in conditionsList) {
            val fieldName = conditions.fieldName
            val value = conditions.value
            val criteriaBuilder = OperationSignCriteriaBuilder()

            val criteria = criteriaBuilder.build(fieldName, value, conditions.sign)
            query.addCriteria(criteria)

        }
        return query
    }
}
