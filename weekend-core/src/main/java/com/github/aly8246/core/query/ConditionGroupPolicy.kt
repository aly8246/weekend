package com.github.aly8246.core.query

import com.github.aly8246.core.handler.Operation
import com.github.aly8246.core.query.bean.ConditionGroup

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 06:45
 * @description：
 * @version:   ：V
 */
interface ConditionGroupPolicy {
    fun conditionGroupPolicy(operation: Operation): ConditionGroup
    fun addConditionGroup(conditionGroup: ConditionGroup): ConditionGroup
}