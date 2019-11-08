package com.github.aly8246.core.resolver.mysql

import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.query.enmu.OperationSignEnum
import com.github.aly8246.core.resolver.Condition
import com.github.aly8246.core.resolver.ConditionEnum
import com.sun.org.apache.xpath.internal.operations.NotEquals
import net.sf.jsqlparser.expression.*
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.expression.operators.relational.*
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.PlainSelect
import org.springframework.util.StringUtils

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/07 下午 05:19
 * @description：
 * @version: ：V
 */
open class SourceConditionResolver {
    //or如果没有括号就是整个一起or
    //左子节点是and
    //右子节点是or
    private fun ergodicTree(e: Expression, conditionList: MutableList<Condition>) {
        when (e) {
            is BinaryExpression -> ergodicTree(e.leftExpression, conditionList)
        }
        when (e) {
            is BinaryExpression -> ergodicTree(e.rightExpression, conditionList)
        }
        if (nodeIsEnd(e)) {
            val deconstructExpressionTree = deconstructExpressionTree(e)
            if (deconstructExpressionTree != null)
                conditionList.add(deconstructExpressionTree)
            else {
                println("可能是or节点:$e")
            }
        }
    }

    open fun sourceBuildCondition(plainSelect: PlainSelect): List<Condition> {
        val rootExpression: BinaryExpression = plainSelect.where as BinaryExpression

        val conditionList = mutableListOf(Condition())
        conditionList.removeAt(0)

        ergodicTree(rootExpression, conditionList)
        conditionList.removeIf { e -> StringUtils.isEmpty(e.fieldName) }
        return conditionList
    }

    private fun deconstructExpressionTree(expression: Expression): Condition? {
        when (expression) {
            is BinaryExpression -> {
                when (expression) {
                    is MinorThan, is MinorThanEquals, is GreaterThan, is AndExpression, is EqualsTo, is NotEqualsTo, is NotEquals -> {
                        val condition = Condition()
                        //and  or
                        condition.type = ConditionEnum.AND

                        //字段或值
                        when (expression.leftExpression) {
                            is LongValue -> condition.value = (expression.leftExpression as LongValue).value
                            is StringValue -> condition.value = (expression.leftExpression as StringValue).value
                            is DoubleValue -> condition.value = (expression.leftExpression as DoubleValue).value
                            is Column -> condition.fieldName = (expression.leftExpression as Column).columnName
                        }
                        when (expression.rightExpression) {
                            is LongValue -> condition.value = (expression.rightExpression as LongValue).value
                            is StringValue -> condition.value = (expression.rightExpression as StringValue).value
                            is DoubleValue -> condition.value = (expression.rightExpression as DoubleValue).value
                            is Column -> condition.value = (expression.leftExpression as Column).columnName
                        }

                        //操作符
                        val comparisonOperator = expression as ComparisonOperator
                        condition.sign = OperationSignEnum.selectOperationSignEnum(comparisonOperator.stringExpression)

                        return condition
                    }
                    else -> throw WeekendException("暂时无法解析的表达式:$expression")
                }
            }
            is Parenthesis -> return null
            else -> return null
        }
    }
//
//
//        //WHERE userMoney < 700 AND (age = 18 OR age = 22) AND 1 = 1 OR 2 = 2
//        val conditions3 = Condition()
//        conditions3.type = ConditionEnum.AND
//        conditions3.fieldName = "age"
//        conditions3.sign = OperationSignEnum.EQ
//        conditions3.value = 18
//        conditions3.group = "1"
//
//        val conditions4 = Condition()
//        conditions4.type = ConditionEnum.OR
//        conditions4.fieldName = "age"
//        conditions4.sign = OperationSignEnum.EQ
//        conditions4.value = 22
//        conditions4.group = "1"
//
//        val conditions5 = Condition()
//        conditions5.type = ConditionEnum.AND
//        conditions5.fieldName = "userMoney"
//        conditions5.sign = OperationSignEnum.LE
//        conditions5.value = 700

    // return listOf(conditions3, conditions4, conditions5)

    /**
     * 如果节点不是MinorThan这种查询节点
     * 如果节点的左子节点和右子节点都不是查询节点
     */

    private fun nodeIsEnd(rootExpression: Expression): Boolean {
        return return when (rootExpression) {
            is BinaryExpression -> {
                when (rootExpression.leftExpression) {
                    is Column -> true
                    is LongValue -> true
                    is StringValue -> true
                    is DoubleValue -> true
                    else -> false
                }
            }
            is Parenthesis -> true
            else -> false
        }
    }
}
