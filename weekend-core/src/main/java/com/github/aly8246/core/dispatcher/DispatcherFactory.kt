package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import java.lang.reflect.Method
import java.sql.Connection
import java.sql.DriverManager

@Suppress("CAST_NEVER_SUCCEEDS")
open class DispatcherFactory<T> : DispatcherFactoryPolicy<T> {
    override fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?): Dispatcher<T> {
        try {
            Class.forName(configuration.driverName)
        } catch (e: ClassNotFoundException) {
            throw WeekendException("配置文件加载失败，要在配置文件加载之前使用必须手动注入初始化配置,或者在配置文件初始化以后再注入bean")
        }

        val connection: Connection = DriverManager.getConnection(configuration.datasourceUrl)

        return OriginalDispatcher(proxy, method, args, connection as MongoConnection)
    }
}