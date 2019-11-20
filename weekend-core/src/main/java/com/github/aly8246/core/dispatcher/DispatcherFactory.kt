package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.BaseMethod
import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.annotation.Page
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoDispatcher
import com.github.aly8246.core.dispatcher.pageDaoHandler.PageDaoDispatcher
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.proxy.WeekendProxy
import com.github.aly8246.core.util.PrintImpl
import java.lang.reflect.Method
import java.sql.Connection
import java.sql.DriverManager

@Suppress("CAST_NEVER_SUCCEEDS")
open class DispatcherFactory<T>(private val weekendProxy: WeekendProxy<T>) : DispatcherFactoryPolicy<T> {
    override fun dispatcherFactory(proxy: Any, method: Method, args: Array<Any>?, target: Class<T>): Dispatcher<T> {
        try {
            Class.forName(configuration.driverName)
        } catch (e: ClassNotFoundException) {
            throw WeekendException("配置文件加载失败，要在配置文件加载之前使用必须手动注入初始化配置,或者在配置文件初始化以后再注入bean")
        }

        PrintImpl().info("WWeekend connect ${configuration.datasourceUrl}")

        val connection: Connection = DriverManager.getConnection(configuration.datasourceUrl)
        weekendProxy.connection = connection

        val command = method.getDeclaredAnnotation(Command::class.java)
        val baseMethod = method.getDeclaredAnnotation(BaseMethod::class.java)
        val page = method.getDeclaredAnnotation(Page::class.java)


        return when {
            //用户普通dao方法
            command != null && baseMethod == null && page == null -> OriginalDispatcher(proxy, method, args, connection as MongoConnection, target)

            //用户重写了通用dao方法
            command != null && baseMethod != null && page == null -> OriginalDispatcher(proxy, method, args, connection as MongoConnection, target)

            //用户使用通用dao方法
            command == null && baseMethod != null && page == null -> BaseDaoDispatcher(proxy, method, args, connection as MongoConnection, target)

            //用户使用通用分页方法
            command == null && baseMethod != null && page != null -> PageDaoDispatcher(proxy, method, args, connection as MongoConnection, target)

            else -> throw WeekendException("不是通用方法或者没有@Command注解")
        }
    }
}