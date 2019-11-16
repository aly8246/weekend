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
    fun insert(sql: String, mongoConnection: MongoConnection): Int
    fun update(sql: String): Int
    fun delete(sql: String): Int
}