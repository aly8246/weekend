package com.github.aly8246.core.executor

import com.mongodb.client.FindIterable
import org.bson.Document
import java.lang.reflect.Parameter

interface Executor {
    fun select(sql: String): FindIterable<Document>
    fun insert(sql: String, param: MutableMap<Parameter, Any?>): Int
    fun update(sql: String): Int
    fun delete(sql: String): Int
}