@file:Suppress("UNCHECKED_CAST")

package com.github.aly8246.weekend.spring.register

import com.github.aly8246.core.proxy.WeekendProxy
import org.springframework.beans.factory.FactoryBean
import java.lang.reflect.Proxy


class WeekendProxyFactory<T>(private val interfaceType: Class<T>) : FactoryBean<T> {

    @Throws(Exception::class)
    override fun getObject(): T? {
        //这里主要是创建接口对应的实例，便于注入到spring容器中
        val daoHandler = WeekendProxy(interfaceType)
        return Proxy.newProxyInstance(daoHandler.javaClass.classLoader, arrayOf<Class<*>>(interfaceType), daoHandler) as T
    }

    override fun getObjectType(): Class<*>? = interfaceType

    override fun isSingleton(): Boolean = true
}
