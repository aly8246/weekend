package com.github.aly8246.core.dispatcher;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.core.util.WeekendResultObj;

import java.lang.reflect.Method;
import java.util.*;

import static com.github.aly8246.core.dispatcher.AbstractDispatcher.regxListParamClass;

public class DispatcherSelector {
protected Object proxy;
protected Method method;
protected Object[] args;
protected RetClass retClass = null;


public DispatcherSelector(Object proxy, Method method, Object[] args) {
	this.proxy = proxy;
	this.method = method;
	this.args = args;
	try {
		this.init();
	} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
		e.printStackTrace();
	}
}

private void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	Exec exec = method.getDeclaredAnnotation(Exec.class);
	
	//step1. 如果包含了集合，则返回集合的class，否则返回实例
	Object returnClassType = WeekendResultObj.getInstance(method.getReturnType());
	
	Object realClassType = null;
	
	//step2.获取返回类型,可能是对象，还有可能是List
	String realClazz = this.method.getReturnType().getCanonicalName();
	
	if (!exec.returnType().equals(Collection.class)) {
		//如果查询注解指定了返回对象
		realClassType = exec.returnType().newInstance();
	} else {
		if (!realClazz.equals("void"))
			try {
				realClassType = Class.forName(realClazz).newInstance();
			} catch (Exception e) {
				//如果是集合接口或者为空则无法成功实例化
				System.out.println(regxListParamClass(this.method.toGenericString()));
				realClassType = Class.forName(regxListParamClass(this.method.toGenericString())).newInstance();
			}
	}
	
	this.retClass = new RetClass(returnClassType, realClassType);
}

public Dispatcher exec() {
	Dispatcher dispatcher = new SimpleDispatcher(this.proxy, this.method, this.args, this.retClass);
	
	//如果包含结果集映射
	if (this.retClass.containerMapping())
		dispatcher = new CustomDispatcher(this.proxy, this.method, this.args, this.retClass);
	
	return dispatcher;
}

}
