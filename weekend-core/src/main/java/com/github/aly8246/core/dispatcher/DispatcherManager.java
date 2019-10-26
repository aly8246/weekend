package com.github.aly8246.core.dispatcher;


import java.lang.reflect.Method;

public class DispatcherManager {
    public Object execDispatcher(Object proxy, Method method, Object[] args) {
        return new DispatcherSelector(proxy, method, args).exec().exec();
    }
}
