package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import java.lang.reflect.Method
import java.sql.Statement

class OriginalDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection) : InitializerDispatcher<T>(proxy, method, args, mongoConnection) {
    //is select
    override fun selectStatement(statement: Statement, command: String): MongoResultSet {
        return statement.executeQuery(command) as MongoResultSet
    }

}