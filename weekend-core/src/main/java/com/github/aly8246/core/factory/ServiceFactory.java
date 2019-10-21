package com.github.aly8246.core.factory;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/21 下午 05:22
 * @description：
 * @version: ：V
 */
public class ServiceFactory<T> implements FactoryBean<T> {
private Class<T> interfaceType;

public ServiceFactory(Class<T> interfaceType) {
	this.interfaceType = interfaceType;
}

@Override
public T getObject() throws Exception {
	//这里主要是创建接口对应的实例，便于注入到spring容器中
	InvocationHandler daoHandler = new WeekendHandler<>(interfaceType);
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
