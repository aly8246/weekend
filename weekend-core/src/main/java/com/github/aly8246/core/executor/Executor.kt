package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import com.mongodb.client.MongoCursor
import org.bson.Document
import java.lang.reflect.Parameter

interface Executor {
    fun select(sql: String): MongoCursor<Document>
    fun insert(sql: String, param: MutableMap<Parameter, Any?>): Int
    fun update(sql: String): Int
    fun delete(sql: String): Int
}