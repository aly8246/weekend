package com.github.aly8246.core.executor

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.util.PrintImpl
import com.mongodb.client.FindIterable
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.bson.Document
import org.bson.conversions.Bson

@Suppress("CAST_NEVER_SUCCEEDS")
class PageExecutor(sql: String, mongoConnection: MongoConnection) : AbstractExecutor(sql, mongoConnection) {
    protected lateinit var select: Select
    protected lateinit var plainSelect: PlainSelect
    protected lateinit var table: Table

    override fun select(sql: String): FindIterable<Document> {

        val condition = this.resolverCondition(plainSelect.where)
        if (configuration.showCondition!!) {
            PrintImpl().debug("table     >>   ${table.name}")
            PrintImpl().debug("condition >>   $condition")
        }

        val collection = this.collection
        val find = collection.find(condition)
        return find
    }

    private fun preSelect(statement: Statement) {
        select = statement as Select
        plainSelect = select.selectBody as PlainSelect
        table = plainSelect.fromItem as Table
    }

    override fun tableName(statement: Statement): String {
        this.preSelect(statement)
        return table.name
    }
}
