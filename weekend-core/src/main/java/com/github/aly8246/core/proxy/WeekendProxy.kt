package com.github.aly8246.core.proxy


import com.github.aly8246.core.dispatcher.DispatcherFactory
import com.github.aly8246.core.dispatcher.DispatcherFactoryPolicy

import java.lang.invoke.MethodHandles
import java.lang.reflect.*


class WeekendProxy<T>(private val target: Class<T>) : InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
        return when {
            Any::class.java == method.declaringClass -> method.invoke(this, args)//class类直接执行
            isDefaultMethod(method) -> invokeDefaultMethod(proxy, method, args)//默认方法直接执行
            else -> {
                val dispatcherFactoryPolicy = DispatcherFactory<T>()
                val dispatcherFactory = dispatcherFactoryPolicy.dispatcherFactory(proxy, method, args)
                dispatcherFactory.execute()
            }
        }

    }


    @Throws(Throwable::class)
    private fun invokeDefaultMethod(proxy: Any, method: Method, args: Array<Any>?): Any {
        val constructor = MethodHandles.Lookup::class.java
                .getDeclaredConstructor(Class::class.java, Int::class.javaPrimitiveType)
        when {
            !constructor.isAccessible -> constructor.isAccessible = true
        }
        val declaringClass = method.declaringClass
        return constructor.newInstance(declaringClass,
                MethodHandles.Lookup.PRIVATE or MethodHandles.Lookup.PROTECTED
                        or MethodHandles.Lookup.PACKAGE or MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args)
    }

    private fun isDefaultMethod(method: Method): Boolean = ((((method.modifiers and (Modifier.ABSTRACT or Modifier.PUBLIC or Modifier.STATIC))) == Modifier.PUBLIC) && method.declaringClass.isInterface)
}
