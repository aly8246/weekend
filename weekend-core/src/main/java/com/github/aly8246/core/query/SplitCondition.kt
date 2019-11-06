package com.github.aly8246.core.query

import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.Operation

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 05:59
 * @description：
 * @version:   ：V
 */
interface SplitCondition {
    fun analysisCommonConditions(operation: Operation): List<Condition>

    fun analysisGroupConditions(operation: Operation): Map<String, List<Condition>>

    fun analysisSortConditions(operation: Operation): List<Condition>

    fun analysisGroupByConditions(operation: Operation): List<Condition>
}