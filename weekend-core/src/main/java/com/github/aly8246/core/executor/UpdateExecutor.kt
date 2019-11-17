package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import com.mongodb.BasicDBObject
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.update.Update

open class UpdateExecutor(sql: String, mongoConnection: MongoConnection) : AbstractExecutor(sql, mongoConnection) {
    protected lateinit var update: Update

    override fun update(sql: String): Int {
        val condition = this.resolverCondition(update.where)
        val collection = this.collection
        val resolverUpdateColumn = this.resolverUpdateColumn()
        val updateMany = collection.updateMany(condition, resolverUpdateColumn)
        return updateMany.modifiedCount.toInt()
    }

    protected fun resolverUpdateColumn(): BasicDBObject {
        val set = BasicDBObject()
        val columns = this.update.columns

        for (index in 0 until columns.count()) {
            set[columns[index].toString()] = this.expressionValue(this.update.expressions[index])
        }

        return BasicDBObject("\$set", set)
    }

    override fun tableName(statement: Statement): String {
        update = statement as Update
        return update.tables[0].name
    }
}