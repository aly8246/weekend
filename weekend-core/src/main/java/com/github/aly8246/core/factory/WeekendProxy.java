package com.github.aly8246.core.factory;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.core.handler.Command;
import com.github.aly8246.core.handler.Condition;
import com.github.aly8246.core.handler.Operation;
import com.github.aly8246.core.handler.SqlCommandHandler;
import com.github.aly8246.core.query.QueryRunner;
import com.github.aly8246.core.query.WeekendQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.aly8246.core.util.MongoTemplateUtil.mongoTemplate;

public class WeekendProxy<T> implements InvocationHandler {
private Class<T> target;

public WeekendProxy(Class<T> target) {
	this.target = target;
}

private String regxListParamClass(String source) {
	System.out.println(source);
	Matcher matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source);
	for (; matcher.find(); ) {
		return matcher.group();
	}
	throw new RuntimeException("异常regxListParamClass");
}

public static void main(String[] args) {
	String source = "public abstract java.util.List<com.github.aly8246.dev.pojo.UserInfo> java.util.List<com.github.aly8246.dev.pojo.UserInfo2> java.util.List<com.github.aly8246.dev.pojo.UserInfo> com.github.aly8246.dev.mdao.TestDao.exec(java.lang.String,java.lang.String)";
	System.err.println(source);
//	System.err.println(source.replace(source.split("(?<=java.util.List<).*?(?=>)")[0], "").replace(source.split("(?<=java.util.List<).*?(?=>)")[1], ""));
//
//	System.err.print(source.split("(?<=java.util.List<).*?(?=>)")[0]);
//	System.out.print("com.github.aly8246.dev.pojo.UserInfo");
//	System.err.println(source.split("(?<=java.util.List<).*?(?=>)")[1]);
	
	String reg = "(?<=java.util.List<).*?(?=>)";
	Pattern p = Pattern.compile(reg);

//让正则对象和要作用的字符串相关联。获取匹配器对象。
	Matcher m = p.matcher(source);
	
	while (m.find()) {
		String result = m.group();
		System.out.println(result);
	}
	
}

@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	if (Object.class.equals(method.getDeclaringClass())) {
		return method.invoke(this, args);
	} else if (isDefaultMethod(method)) {
		return invokeDefaultMethod(proxy, method, args);
	}
	Class<?> returnType = method.getReturnType();
	
	Exec exec = this.getExec(method);
	String baseCommand = this.getBaseCommand(exec);
	
	//获取执行命令
	Operation run = this.getCommand(exec).run(baseCommand);
	
	
	//组装query
	List<Condition> conditionList = run.getConditionList();
	WeekendQuery weekendQuery = new QueryRunner();
	Query query = weekendQuery.run(conditionList);
	
	
	//执行并返回
	
	//如果交给exec，需要 TODO 传递参数值 传递方法相关 返回结果相关
	
	if (run.getOperation().equals("select")) {
		if (returnType.equals(List.class)) {
			String realClass = this.regxListParamClass(method.toGenericString());
			
			return mongoTemplate.find(query, Class.forName(realClass).newInstance().getClass(), run.getTableName());
			
		} else if (returnType.equals(Object.class)) {//TODO 如果需要分页,需要单独处理
			System.out.println("something");
		} else {
			return mongoTemplate.findOne(query, returnType, run.getTableName());
		}
	} else if (run.getOperation().equals("update")) {
	
	}
	
	
	return null;
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
