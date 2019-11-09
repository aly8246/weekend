package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.configuration.ConfigurationUtil.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

@Suppress("CAST_NEVER_SUCCEEDS")
open class DispatcherFactory<T> : DispatcherFactoryPolicy<T> {
    override fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?): Dispatcher<T> {

        Class.forName(configuration.driverName)

        val connection: Connection = DriverManager.getConnection(configuration.datasourceUrl)

        configuration.connection = connection
        configuration.mongoConnection = connection as MongoConnection

        return OriginalDispatcher(proxy, method, args)
    }
}