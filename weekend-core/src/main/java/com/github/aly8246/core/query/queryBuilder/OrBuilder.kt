package com.github.aly8246.core.query.queryBuilder

import com.github.aly8246.core.handler.Conditions
import com.github.aly8246.core.query.queryBuilder.basic.CriteriaBuilder
import com.github.aly8246.core.query.queryBuilder.basic.OperationSignCriteriaBuilder
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
class OrBuilder(private val query: Query) : QueryBuilder {

    override fun buildQuery(conditionsList: List<Conditions>): Query {

        //条件构建器，如果是where或者and则一起,如果是排序则是其他构建器
        val criteriaArray = arrayOfNulls<Criteria>(conditionsList.size)
        for (i in conditionsList.indices) {
            val conditions = conditionsList[i]

            val fieldName = conditions.fieldName
            val value = conditions.value
            val criteriaBuilder = OperationSignCriteriaBuilder()
            val criteria = criteriaBuilder.build(fieldName, value, conditions.sign)
            criteriaArray[i] = criteria
        }
        query.addCriteria(Criteria().orOperator(*criteriaArray))
        return query
    }
}
