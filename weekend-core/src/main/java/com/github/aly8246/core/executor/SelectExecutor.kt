package com.github.aly8246.core.executor

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.util.PrintImpl
import com.mongodb.client.MongoCursor
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.bson.Document

class SelectExecutor(sql: String) : AbstractExecutor(sql), Executor {
    override fun select(sql: String, mongoConnection: MongoConnection): MongoCursor<Document> {
        val select = statement as Select
        val plainSelect: PlainSelect = select.selectBody as PlainSelect
        val table = plainSelect.fromItem as Table

        // val selectField = this.selectField(plainSelect)
        val query = this.resolverCondition(plainSelect.where)

        if (configuration.showCondition!!) {
            PrintImpl().debug("table     >>   ${table.name}")
            PrintImpl().debug("condition >>   $query")
        }

        val collection = mongoConnection.getCollection(table)
        val find = collection.find(query)

        //todo limit

        //todo groupBy

        //val mongoCursor = super.select(sql, mongoConnection)
        return find.cursor()
    }
}
