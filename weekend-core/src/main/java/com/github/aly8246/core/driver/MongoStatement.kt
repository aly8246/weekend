package com.github.aly8246.core.driver

import com.github.aly8246.core.executor.DeleteExecutor
import com.github.aly8246.core.executor.InsertExecutor
import com.github.aly8246.core.executor.SelectExecutor
import com.github.aly8246.core.executor.UpdateExecutor
import com.mongodb.client.MongoCursor
import org.bson.Document
import java.lang.Exception
import java.lang.reflect.Parameter
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLWarning
import java.sql.Statement

open class MongoStatement(
        private var mongoConnection: MongoConnection
        , resultSetType: Int
        , resultSetConcurrency: Int
        , resultSetHoldability: Int
) : Statement {

    private var cursor: MongoCursor<Document>? = null
    private var closed = false
    private lateinit var result: ResultSet
    lateinit var param: MutableMap<String, Any?>

    override fun clearBatch() {
    }

    override fun getResultSetType(): Int = 1

    override fun isCloseOnCompletion(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMaxRows(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getConnection(): Connection {
        return mongoConnection
    }

    override fun setMaxFieldSize(max: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWarnings(): SQLWarning {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun executeQuery(sql: String): ResultSet {
        //val startTime = System.currentTimeMillis()
        val selectExecutor = SelectExecutor(sql, connection as MongoConnection)
        cursor = selectExecutor.select(sql).cursor()
        // val endTime = System.currentTimeMillis()
        //  val seconds = (endTime - startTime) / 1000f
        // PrintImpl().info(" executeQuery.cursor spend time: $seconds seconds.")

        val mongoResultSet = MongoResultSet(cursor!!, connection as MongoConnection, this)
        this.result = mongoResultSet
        return mongoResultSet
    }

    //关闭连接
    override fun close() {
        cursor?.close()
        closed = true
        try {
            mongoConnection.close()
        } catch (e: Exception) {

        }
        try {
            this.resultSet.close()
        } catch (e: Exception) {

        }
    }

    //连接是否被关闭
    override fun isClosed(): Boolean {
        return closed
    }

    override fun getMaxFieldSize(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUpdateCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMaxRows(max: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFetchDirection(direction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFetchSize(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun setEscapeProcessing(enable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCursorName(name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(sql: String): Boolean {
        val deleteExecutor = DeleteExecutor(sql, connection as MongoConnection)
        val delete = deleteExecutor.delete(sql)
        this.result = MongoResultSet(delete)
        return true
    }

    override fun execute(sql: String?, autoGeneratedKeys: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(sql: String?, columnIndexes: IntArray?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(sql: String?, columnNames: Array<out String>?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPoolable(poolable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun executeBatch(): IntArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQueryTimeout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFetchSize(rows: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearWarnings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isPoolable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addBatch(sql: String) {
        val insertExecutor = InsertExecutor(sql, connection as MongoConnection)
        val insert = insertExecutor.insert(sql, this.param)
        this.result = MongoResultSet(insert)
    }


    override fun getGeneratedKeys(): ResultSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultSetConcurrency(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultSet(): ResultSet {
        return this.result
    }

    override fun setQueryTimeout(seconds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeOnCompletion() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun executeUpdate(sql: String): Int {
        val updateExecutor = UpdateExecutor(sql, mongoConnection)
        val update = updateExecutor.update(sql)
        this.result = MongoResultSet(update)

        return update
    }

    override fun executeUpdate(sql: String?, autoGeneratedKeys: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun executeUpdate(sql: String?, columnIndexes: IntArray?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun executeUpdate(sql: String?, columnNames: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFetchDirection(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultSetHoldability(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMoreResults(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMoreResults(current: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}