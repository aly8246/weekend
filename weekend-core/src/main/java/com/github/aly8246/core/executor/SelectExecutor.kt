package com.github.aly8246.core.executor

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.util.PrintImpl
import com.mongodb.client.MongoCursor
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.bson.Document

@Suppress("CAST_NEVER_SUCCEEDS")
class SelectExecutor(sql: String, mongoConnection: MongoConnection) : AbstractExecutor(sql, mongoConnection) {
    protected lateinit var select: Select
    protected lateinit var plainSelect: PlainSelect
    protected lateinit var table: Table

    override fun select(sql: String): MongoCursor<Document> {
        val selectField = this.selectField(plainSelect)

        val condition = this.resolverCondition(plainSelect.where)
        val orderBy = this.resolverOrderBy(plainSelect)
        if (configuration.showCondition!!) {
            PrintImpl().debug("table     >>   ${table.name}")
            PrintImpl().debug("condition >>   $condition")
            PrintImpl().debug("field     >>   $selectField")
        }


        val collection = this.collection
        val find = collection.find(condition)

        find.projection(selectField)
        find.sort(orderBy)

        if (plainSelect.limit != null) {
            val offset = plainSelect.limit.offset
            if (offset != null) find.skip(offset.toString().toInt())
            val rowCount = plainSelect.limit.rowCount
            find.limit(rowCount.toString().toInt())
        }
        return find.cursor()
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
