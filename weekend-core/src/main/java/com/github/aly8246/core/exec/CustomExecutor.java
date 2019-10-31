package com.github.aly8246.core.exec;

import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.aly8246.core.util.MongoTemplateUtil.mongoTemplate;

public class CustomExecutor extends AbstractExecutor {


@Override
public void insert() {
	super.insert();
}

@Override
public void delete() {
	super.delete();
}

@Override
public int update() {
	return super.update();
}

@Override
public Object select(Query query, Class<?> returnType, String tableName, Method method) {
	System.err.println("还未实现映射结果集的方法");
	//TODO
	//  step1. 根据returnTpe 来new一个对象
	//  step2. 使用map返回类的mongoTemplate来查询
	Map one = mongoTemplate.findOne(query, Map.class, tableName);
	System.out.println("查询结果如下，但是我没时间给你转，所以给你返回一个空的实例");
	one.entrySet().forEach(System.err::println);
	System.out.println("=====================================================");
	
	
	//  step3. 根据执行得到的结果来组装新数据
//	try {
//		return returnType.newInstance();
//	} catch (InstantiationException | IllegalAccessException e) {
//		e.printStackTrace();
//	}
	
	return null;
	// return super.select(query, returnType, tableName);
}

@Override
public List<Object> selectList(Query query, Class<?> returnType, String tableName, Method method) {
	System.err.println("还未实现映射结果集的方法");
	
	List<Map> listMap = mongoTemplate.find(query, Map.class, tableName);
	System.out.println("查询结果如下，但是我没时间给你转，所以给你返回null");
	listMap.forEach(e -> e.entrySet().forEach(System.err::println));
	System.out.println("=====================================================");

//        return new ArrayList<Object>() {{
//            try {
//                add(returnType.newInstance());
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }};
	return new ArrayList<>();
}
}
