package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.util.PrintImpl
import com.mongodb.MongoBulkWriteException
import com.mongodb.MongoWriteException
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.operators.relational.ExpressionList
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.bson.Document
import java.lang.reflect.Parameter

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/15 上午 11:54
 * @description：
 * @version:   ：V
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class InsertExecutor(sql: String, mongoConnection: MongoConnection) : AbstractExecutor(sql, mongoConnection) {
    protected lateinit var insert: Insert
    protected lateinit var table: Table

    override fun insert(sql: String, param: MutableMap<String, Any?>): Int {
        super.insert(sql, param)

        val collection = this.collection
        val columns = insert.columns

        //根据mapping注解来替换数据库里的字段
        val insertCount: Int
        when (insert.itemsList) {
            //批量新增
            is MultiExpressionList -> {
                val documentList: MutableList<Document> = mutableListOf()
                val exprList = (insert.itemsList as MultiExpressionList).exprList
                for (expressionList in exprList) {
                    val expressions = expressionList.expressions
                    val document = this.buildDocument(columns, expressions)
                    documentList.add(document)
                }

                insertCount = try {
                    collection.insertMany(documentList)
                    documentList.size
                } catch (e: MongoBulkWriteException) {
                    val message = e.message
                    PrintImpl().error("重复的主键ID: $message")
                    0
                }
            }
            //单个新增
            is ExpressionList -> {
                val expressions = (insert.itemsList as ExpressionList).expressions
                val document = this.buildDocument(columns, expressions)

                insertCount = try {
                    collection.insertOne(document)
                    1
                } catch (e: MongoWriteException) {
                    val message = e.message
                    PrintImpl().error("重复的主键ID: $message")
                    0
                }
            }
            else -> throw WeekendException("暂时无法解析的sql: >> $insert")
        }

        return insertCount
    }

    private fun buildDocument(columns: MutableList<Column>, expressionList: MutableList<Expression>): Document {
        val document = Document()
        for (index in 0 until expressionList.count()) {
            document[columns[index].toString().replace("`", "")] = this.expressionValue(expressionList[index] as Expression)
        }
        return document
    }


    private fun preInsert(statement: Statement) {
        insert = statement as Insert
        table = insert.table
    }

    override fun tableName(statement: Statement): String {
        this.preInsert(statement)
        return table.name
    }
}