package com.github.aly8246.core.executor

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.util.PrintImpl
import com.mongodb.DBCursor
import com.mongodb.client.MongoCursor
import com.mongodb.client.internal.MongoBatchCursorAdapter
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.bson.Document

@Suppress("CAST_NEVER_SUCCEEDS")
class SelectExecutor(sql: String) : AbstractExecutor(sql), Executor {
    override fun select(sql: String, mongoConnection: MongoConnection): MongoCursor<Document> {
        val select = statement as Select
        val plainSelect: PlainSelect = select.selectBody as PlainSelect
        val table = plainSelect.fromItem as Table

        val selectField = this.selectField(plainSelect)
        val query = this.resolverCondition(plainSelect.where)
        val orderBy = this.resolverOrderBy(plainSelect)
        if (configuration.showCondition!!) {
            PrintImpl().debug("table     >>   ${table.name}")
            PrintImpl().debug("condition >>   $query")
            PrintImpl().debug("field     >>   $selectField")
        }

        val collection = mongoConnection.getCollection(table)
        val find = collection.find(query)

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
}
