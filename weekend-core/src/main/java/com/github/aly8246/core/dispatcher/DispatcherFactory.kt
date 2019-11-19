package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.BaseMethod
import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoDispatcher
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


        val annotationList = method.declaredAnnotations.toMutableList()

        //这一步有个坑，当用户dao继承BaseDao的时候，方法上面同时有两个注解
        //@Command || @BaseMethod
        //如果存在@Command则说明用户重写了baseDao里的方法，理当执行用户自己的操作
        if (annotationList.stream().anyMatch { e -> e.annotationClass == Command::class }) {
            return OriginalDispatcher(proxy, method, args, connection as MongoConnection, target)
        } else if (annotationList.stream().anyMatch { e -> e.annotationClass == BaseMethod::class }) {
            return BaseDaoDispatcher(proxy, method, args, connection as MongoConnection, target)
        }
        throw WeekendException("不是通用方法或者没有@Command注解")
    }
}