package com.github.aly8246.core.factory;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.core.handler.AbstractCommand;
import com.github.aly8246.core.handler.Command;
import com.github.aly8246.core.handler.SqlCommandHandler;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class WeekendProxy<T> implements InvocationHandler {
private Class<T> target;

public WeekendProxy(Class<T> target) {
	this.target = target;
}

@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	
	if (Object.class.equals(method.getDeclaringClass())) {
		return method.invoke(this, args);
	} else if (isDefaultMethod(method)) {
		return invokeDefaultMethod(proxy, method, args);
	}
	//基础功能
	//将sql解析
	//组装mongo需要的查询条件
	//执行条件查询
	
	//TODO 获取返回值，查询注解
	Exec exec = this.getExec(method);
	String sql = this.getBaseSql(exec);
	
	
	Class<?> returnType = method.getReturnType();
	
	System.out.println("执行的sql  :  " + sql);
	System.out.println("返回值     :  " + returnType);
	
	//将得到的原始命令交由Command接口处理
	
	//写一个sqlHandler，调用valid方法
	Command command = this.getCommand(exec);
	command.validCommand();


//	for (int i = 0; i < args.length; i++) {
//		System.err.println(args[i]);
//		args[i].getClass();
//	}
//	Optional<Object> param = Arrays.stream(args).collect(Collectors.toList()).stream().findAny();
//	if (param.isPresent()) {
//		System.out.println(param.get());
//		List<Object> collect = (List<Object>) param.get();
//		if (collect.stream().anyMatch("小黄"::equals)) {
//			System.err.println("小黄不能有，我给你换成小张了");
//			return "小张--小黄不能有，我给你换成小张了";
//		}
//	}
//
	
	return "param.isPresent()";
}

private Exec getExec(Method method) {
	Exec exec = method.getDeclaredAnnotation(Exec.class);
	if (exec == null) throw new RuntimeException("必须为weekend的接口方法指定SQL");
	return exec;
}

private String getBaseSql(Exec exec) {
	String sql = Arrays.stream(exec.value()).collect(Collectors.joining());
	if (StringUtils.isEmpty(sql)) throw new RuntimeException("SQL不能为空");
	return sql;
}

private Command getCommand(Exec exec) {
	Class<? extends Command> handler = exec.handler();
	try {
		return handler.newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
		e.printStackTrace();
	}
	return new SqlCommandHandler();
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
