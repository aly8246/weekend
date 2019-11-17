package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.delete.Delete

open class DeleteExecutor(sql: String, mongoConnection: MongoConnection) : AbstractExecutor(sql, mongoConnection) {
    private lateinit var delete: Delete
    override fun delete(sql: String): Int {
        val condition = this.resolverCondition(delete.where)
        val deleteMany = this.collection.deleteMany(condition)
        return deleteMany.deletedCount.toInt()
    }

    override fun tableName(statement: Statement): String {
        delete = this.statement as Delete
        return delete.table.name
    }
}