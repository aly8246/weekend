package com.github.aly8246.core.factory;

import com.github.aly8246.core.annotation.Exec;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class WeekendProxy<T> implements InvocationHandler {
    private Class<T> target;

    public WeekendProxy(Class<T> target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (isDefaultMethod(method)) {
                return invokeDefaultMethod(proxy, method, args);
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            throw new RuntimeException("xx");
        }

        //TODO 获取返回值，查询注解
        Exec exec = method.getDeclaredAnnotation(Exec.class);
        String value = exec.value();
        System.out.println("执行的sql  :  " + value);
        System.out.println("返回值     :  " + method.getReturnType());


        List<Object> params = Arrays.stream(args).collect(Collectors.toList());
        if (params.stream().anyMatch("小黄"::equals)) {
            System.err.println("小黄不能有，我给你换成小张了");
            return "小张--小黄不能有，我给你换成小张了";
        }

        return params.get(0);
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
