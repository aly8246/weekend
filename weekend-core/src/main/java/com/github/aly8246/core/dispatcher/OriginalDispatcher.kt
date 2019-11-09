package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method
import java.sql.Connection
import java.sql.Statement

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA", "UNCHECKED_CAST")
class OriginalDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, statement: Statement, connection: Connection) : InitializerDispatcher<T>(proxy, method, args,statement,connection) {

}