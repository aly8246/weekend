package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.client.MongoCursor
import net.sf.jsqlparser.expression.*
import net.sf.jsqlparser.expression.Function
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.expression.operators.conditional.OrExpression
import net.sf.jsqlparser.expression.operators.relational.*
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.AllColumns
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.SelectExpressionItem
import net.sf.jsqlparser.statement.select.SelectItem
import org.bson.Document
import org.bson.conversions.Bson
import java.util.regex.Pattern

interface Executor {
    fun select(sql: String, mongoConnection: MongoConnection): MongoCursor<Document>
    fun insert(sql: String): Int
    fun update(sql: String): Int
    fun delete(sql: String): Int

    private fun expressionName(expression: Expression): String {
        return when (expression) {
            is StringValue -> expression.toString()
            is Column -> expression.columnName
            else -> throw  RuntimeException("无法解析的字段")
        }
    }

    fun expressionValue(expression: Expression): Any? {
        return when (expression) {
            is StringValue -> expression.value
            is LongValue -> expression.value
            is DoubleValue -> expression.value
            is DateValue -> expression.value
            is TimeValue -> expression.value
            is TimestampValue -> expression.value
            is NullValue -> null
            is JdbcParameter -> throw java.lang.RuntimeException("暂时无法解析:$expression")
            else -> throw java.lang.RuntimeException("暂时无法解析:$expression")
        }
    }

    fun resolverCondition(expression: Expression?): Bson {
        val basicDBObject = BasicDBObject()
        if (expression == null) return BasicDBObject()
        //如果是基本类型则直接处理掉
        resolverConditionTree(expression, basicDBObject)
        return basicDBObject
    }

    fun selectField(plainSelect: PlainSelect): BasicDBObject {
        val fields = BasicDBObject()
        plainSelect.selectItems.forEach { e ->
            run {
                when (val selectItem = e as SelectItem) {
                    is AllColumns -> if (fields.size > 0) throw RuntimeException("AllColumns")
                    is SelectExpressionItem -> {
                        when (selectItem) {
                            is Function -> println(selectItem)
                            else -> fields[expressionName(selectItem.expression)] = 1
                        }
                    }
                    else -> throw RuntimeException("selectItem no")
                }
            }
        }
        return fields
    }


    private fun resolverConditionTree(expression: Expression, basicDBObject: BasicDBObject) {
        when (expression) {
            is AndExpression -> {
                resolverConditionTree(expression.leftExpression, basicDBObject)
                resolverConditionTree(expression.rightExpression, basicDBObject)
            }
            is OrExpression -> {
                val orDBObject = arrayOf(BasicDBObject(), BasicDBObject())
                resolverConditionTree(expression.leftExpression, orDBObject[0])
                resolverConditionTree(expression.rightExpression, orDBObject[1])
                basicDBObject["%or"] = orDBObject
            }
            is Parenthesis -> resolverConditionTree(expression.expression, basicDBObject)
            //是关联查询的大于，大于等于，小于等于等等
            is EqualsTo -> basicDBObject[expressionName(expression.leftExpression)] = expressionValue(expression.rightExpression)
            is EqualsTo -> appendCondition(basicDBObject, "\$lt", expression)
            is NotEqualsTo -> basicDBObject[expressionName(expression.leftExpression)] = BasicDBObject().put("\$ne", expressionValue(expression.rightExpression))
            is GreaterThan -> appendCondition(basicDBObject, "\$gt", expression)
            is MinorThan -> appendCondition(basicDBObject, "\$lt", expression)
            is GreaterThanEquals -> appendCondition(basicDBObject, "\$gte", expression)
            is MinorThanEquals -> appendCondition(basicDBObject, "\$lte", expression)
            is Between -> {
                val dbObject: DBObject = BasicDBObject()
                dbObject.put("\$gte", expressionValue(expression.betweenExpressionStart))
                dbObject.put("\$lte", expressionValue(expression.betweenExpressionEnd))
                basicDBObject[expressionName(expression.leftExpression)] = dbObject
            }
            is InExpression -> {
                val expressionList = expression.rightItemsList as ExpressionList
                val valueList = expressionList.expressions
                val inArr = arrayOfNulls<Any?>(valueList.size)

                var i = 0
                valueList.forEach { e ->
                    run {
                        inArr[i++] = expressionValue(e as Expression)
                    }
                }
                val dbObject: DBObject = BasicDBObject()

                if (expression.isNot) dbObject.put("\$nin", inArr)
                else dbObject.put("\$in", inArr)

                basicDBObject[expressionName(expression.leftExpression)] = dbObject
            }
            is LikeExpression -> {
                var value = (expression.rightExpression as StringValue).value
                value = if (value.startsWith("%")) value.substring(1) else "^$value"
                value = if (value.endsWith("%")) value.substring(0, value.length - 1) else "$value$"
                value = value.replace("%", ".*")
                val pattern: Pattern? = Pattern.compile(value, Pattern.CASE_INSENSITIVE)
                basicDBObject[expressionName(expression.leftExpression)] = pattern
            }
            else -> throw RuntimeException("暂时无法解析的表达式:$expression")
        }
    }

    private fun appendCondition(basicDBObject: BasicDBObject, sign: String, expression: Expression) {
        when (expression) {
            is BinaryExpression -> {
                val expressionName = expressionName(expression.leftExpression)
                val expressionValue = expressionValue(expression.rightExpression)

                val dbObject = basicDBObject.get(expressionName)
                if (dbObject != null) {
                    dbObject as BasicDBObject
                    dbObject.append(sign, expressionValue)
                } else {
                    val obj: DBObject = BasicDBObject()
                    obj.put(sign, expressionValue)
                    basicDBObject[expressionName] = obj
                }
            }
        }
    }

}