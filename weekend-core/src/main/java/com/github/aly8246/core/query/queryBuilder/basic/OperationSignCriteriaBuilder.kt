package com.github.aly8246.core.query.queryBuilder.basic

import com.github.aly8246.core.query.enmu.OperationEnum
import com.github.aly8246.core.query.enmu.OperationEnum.*
import org.springframework.data.mongodb.core.query.Criteria

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/30 下午 05:52
 * @description：
 * @version: ：V
 */
class OperationSignCriteriaBuilder : CriteriaBuilder {
    override fun build(fieldName: String, value: Any, sign: OperationEnum): Criteria {
        var criteria: Criteria? = null
        when (sign) {
            GT -> criteria = Criteria.where(fieldName).gt(value)   //  >
            LT -> criteria = Criteria.where(fieldName).lt(value)   //  <
            EQ -> criteria = Criteria.where(fieldName).`is`(value) //  =
            GE -> criteria = Criteria.where(fieldName).gte(value)  //  >=
            LE -> criteria = Criteria.where(fieldName).lte(value)  //  <=
            NQ -> criteria = Criteria.where(fieldName).ne(value)   //  !=
            // in...
            IN -> criteria = Criteria.where(fieldName).`in`(listOf(*value.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        }
        return criteria
    }
}
