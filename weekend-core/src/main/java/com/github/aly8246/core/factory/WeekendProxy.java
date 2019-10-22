package com.github.aly8246.core.factory;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.core.handler.Command;
import com.github.aly8246.core.handler.Operation;
import com.github.aly8246.core.handler.SqlCommandHandler;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;

import static com.github.aly8246.core.factory.WeekendBeanFactory.mongoTemplate;

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
	
	//TODO 获取返回值，查询注解
	Exec exec = this.getExec(method);
	String baseCommand = this.getBaseCommand(exec);
	
	
	Class<?> returnType = method.getReturnType();
	
	System.out.println("返回值     :  " + returnType);
	
	Operation run = this.getCommand(exec).run(baseCommand);
	System.out.println(run);
	
	System.out.println(mongoTemplate);
	Object one = mongoTemplate.findOne(new Query(), returnType, run.getTableName());
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
	
	return one;
	
}

private Exec getExec(Method method) {
	Exec exec = method.getDeclaredAnnotation(Exec.class);
	if (exec == null) throw new RuntimeException("必须为weekend的接口方法指定SQL");
	return exec;
}

private String getBaseCommand(Exec exec) {
	String baseCommand = String.join("", exec.value());
	if (StringUtils.isEmpty(baseCommand)) throw new RuntimeException("BaseCommand不能为空");
	return baseCommand;
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
