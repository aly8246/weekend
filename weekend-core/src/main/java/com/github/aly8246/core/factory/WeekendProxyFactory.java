package com.github.aly8246.core.factory;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


public class WeekendProxyFactory<T> implements FactoryBean<T> {
private Class<T> interfaceType;

public WeekendProxyFactory(Class<T> interfaceType) {
	this.interfaceType = interfaceType;
}

@Override
public T getObject() throws Exception {
	//这里主要是创建接口对应的实例，便于注入到spring容器中
	InvocationHandler daoHandler = new WeekendProxy<>(interfaceType);
	return (T) Proxy.newProxyInstance(daoHandler.getClass().getClassLoader(),
			new Class[]{interfaceType}, daoHandler);
}

@Override
public Class<?> getObjectType() {
	return interfaceType;
}

@Override
public boolean isSingleton() {
	return true;
}
}
