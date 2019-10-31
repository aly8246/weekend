package com.github.aly8246.core.query

import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.handler.Conditions
import com.github.aly8246.core.handler.Operation
import com.github.aly8246.core.handler.QueryEnum
import org.springframework.util.StringUtils
import java.util.stream.Collectors

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 06:11
 * @description：
 * @version:   ：V
 */
open class BaseAnalysisQueryCondition : AnalysisCondition {
    override fun analysisCommonConditions(operation: Operation): List<Conditions> {
        return operation.conditionsList
                .stream()
                .filter { e -> StringUtils.isEmpty(e.group) }
                .filter { e -> e.type == QueryEnum.WHERE || e.type == QueryEnum.AND }
                .collect(Collectors.toList<Conditions>())
    }

    override fun analysisGroupConditions(operation: Operation): Map<String, List<Conditions>> {
        return operation.conditionsList
                .stream()
                .filter { e -> e.type == QueryEnum.OR || !StringUtils.isEmpty(e.group) }
                .collect(Collectors.groupingBy<Conditions, String> { it.group })
    }

    override fun analysisSortConditions(operation: Operation): List<Conditions> {
        println("排序组装器还未实现")
        return emptyList()
    }

    override fun analysisGroupByConditions(operation: Operation): List<Conditions> {
        println("分组组装器还未实现")
        return emptyList()
    }
}