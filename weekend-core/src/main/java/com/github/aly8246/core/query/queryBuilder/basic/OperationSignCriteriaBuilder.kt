package com.github.aly8246.core.query.queryBuilder.basic

import com.github.aly8246.core.query.enmu.OperationEnum
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
            OperationEnum.GT//  >
            -> criteria = Criteria.where(fieldName).gt(value)
            OperationEnum.LT//  <
            -> criteria = Criteria.where(fieldName).lt(value)
            OperationEnum.EQ//  =
            -> criteria = Criteria.where(fieldName).`is`(value)
            OperationEnum.GE//  >=
            -> criteria = Criteria.where(fieldName).gte(value)
            OperationEnum.LE//  <=
            -> criteria = Criteria.where(fieldName).lte(value)
            OperationEnum.NQ// !=
            -> criteria = Criteria.where(fieldName).ne(value)
            OperationEnum.IN// in...
            -> criteria = Criteria.where(fieldName).`in`(listOf(*value.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        }
        return criteria
    }
}
