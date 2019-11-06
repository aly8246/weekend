package com.github.aly8246.core.query

import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.Operation
import com.github.aly8246.core.resolver.ConditionEnum
import org.springframework.util.StringUtils
import java.util.stream.Collectors

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 06:11
 * @description：
 * @version:   ：V
 */
open class SplitQueryCondition : SplitCondition {
    override fun analysisCommonConditions(operation: Operation): List<Condition> {
        return operation.conditionList
                .stream()
                .filter { e -> StringUtils.isEmpty(e.group) }
                .filter { e -> e.type == ConditionEnum.WHERE || e.type == ConditionEnum.AND }
                .collect(Collectors.toList<Condition>())
    }

    override fun analysisGroupConditions(operation: Operation): Map<String, List<Condition>> {
        return operation.conditionList
                .stream()
                .filter { e -> e.type == ConditionEnum.OR || !StringUtils.isEmpty(e.group) }
                .collect(Collectors.groupingBy<Condition, String> { it.group })
    }

    override fun analysisSortConditions(operation: Operation): List<Condition> {
        return operation.conditionList
                .stream()
                .filter { e -> StringUtils.isEmpty(e.group) }
                .filter { e -> e.type == ConditionEnum.SORT }
                .collect(Collectors.toList<Condition>())
    }

    override fun analysisGroupByConditions(operation: Operation): List<Condition> {
        return operation.conditionList
                .stream()
                .filter { e -> StringUtils.isEmpty(e.group) }
                .filter { e -> e.type == ConditionEnum.GROUP }
                .collect(Collectors.toList<Condition>())
    }
}