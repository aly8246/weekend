package com.github.aly8246.core.dispatcher

import java.lang.reflect.Method
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

open class DispatcherFactory<T> : DispatcherFactoryPolicy<T> {
    override fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?): Dispatcher<T> {
        Class.forName("com.github.aly8246.core.driver.MongoDriver")
        val connection: Connection = DriverManager.getConnection("jdbc:mongodb://148.70.16.82/weekend-dev")
        val statement: Statement = connection.createStatement()
        return OriginalDispatcher(proxy, method, args, statement, connection)
    }
}