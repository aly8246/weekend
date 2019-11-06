package com.github.aly8246.core.query.bean

import com.github.aly8246.core.resolver.Condition

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 06:34
 * @description：
 * @version:   ：V
 */
class ConditionGroup {
    lateinit var commonConditions: List<Condition>
    lateinit var groupCondition: Map<String, List<Condition>>
    lateinit var sortConditions: List<Condition>
    lateinit var groupByConditions: List<Condition>
}