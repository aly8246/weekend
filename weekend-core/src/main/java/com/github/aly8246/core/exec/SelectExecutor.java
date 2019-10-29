package com.github.aly8246.core.exec;

import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.aly8246.core.util.MongoTemplateUtil.mongoTemplate;

public class SelectExecutor extends BaseExecutor {

@Override
public Object select(Query query, Class<?> returnType, String tableName, Method method) {
	return mongoTemplate.findOne(query, returnType, tableName);
}

@Override
public List<Object> selectList(Query query, Class<?> aClass, String tableName, Method method) {
	List<?> objects = mongoTemplate.find(query, aClass, tableName);
	return (List<Object>) objects;
}
}
