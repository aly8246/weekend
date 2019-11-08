package com.github.aly8246.core.resolver.mysql

import net.sf.jsqlparser.expression.*
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.expression.operators.conditional.OrExpression
import net.sf.jsqlparser.expression.operators.relational.EqualsTo
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.PlainSelect

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/07 下午 05:19
 * @description：
 * @version: ：V
 */
class SourceConditionResolver {
    fun sourceBuildCondition(plainSelect: PlainSelect) {
        var rootExpression: BinaryExpression = plainSelect.where as BinaryExpression ?: return

        loop@ while (true) {
            val rightExpression = rootExpression.rightExpression
            val leftExpression = rootExpression.leftExpression

            when (rightExpression) {
                is BinaryExpression -> println("BinaryExpression条件判断")
                is Parenthesis -> println("组合类型")
                else -> println("未知类型")
            }
            when (leftExpression) {
                is BinaryExpression -> println("BinaryExpression条件判断")
                is Parenthesis -> println("组合类型")
                else -> println("未知类型")
            }

            when {
                !nodeEnd(leftExpression) -> rootExpression = leftExpression as BinaryExpression
            }
            when {
                !nodeEnd(rightExpression) -> rootExpression = rightExpression as BinaryExpression
            }
            when {
                nodeEnd(leftExpression) && nodeEnd(rightExpression) -> break@loop
            }
        }

    }

    /**
     * 如果节点不是MinorThan这种查询节点
     * 如果节点的左子节点和右子节点都不是查询节点
     */
    private fun nodeEnd(rootExpression: Expression): Boolean = when (rootExpression) {
        is Parenthesis -> false  //是组合类型
        is Column, is LongValue, is StringValue, is DoubleValue -> true //是基本类型和字段
        else -> when ((rootExpression as BinaryExpression).leftExpression) { //是BinaryExpression条件判断
            is Parenthesis -> false
            !is BinaryExpression -> false
            else -> true
        }
    }
}
