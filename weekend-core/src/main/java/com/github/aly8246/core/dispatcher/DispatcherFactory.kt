package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method
import java.sql.Connection
import java.sql.DriverManager

@Suppress("CAST_NEVER_SUCCEEDS")
open class DispatcherFactory<T> : DispatcherFactoryPolicy<T> {
    override fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?): Dispatcher<T> {
        Class.forName(configuration.driverName)

        val connection: Connection = DriverManager.getConnection(configuration.datasourceUrl)

        return OriginalDispatcher(proxy, method, args, connection as MongoConnection)
    }
}