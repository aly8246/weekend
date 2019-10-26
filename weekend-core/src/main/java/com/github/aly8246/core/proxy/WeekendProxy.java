package com.github.aly8246.core.proxy;


import com.github.aly8246.core.dispatcher.DispatcherManager;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;


public class WeekendProxy<T> implements InvocationHandler {
    private Class<T> target;

    public WeekendProxy(Class<T> target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);//class类直接执行
        } else if (isDefaultMethod(method)) {
            return invokeDefaultMethod(proxy, method, args);//默认方法直接执行
        }
        return new DispatcherManager().execDispatcher(proxy, method, args);//接口方法产生代理
    }


    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor.newInstance(declaringClass,
                MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                        | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
                && method.getDeclaringClass().isInterface();
    }
}
