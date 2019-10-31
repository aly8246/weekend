package com.github.aly8246.core.exec;

import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.List;

public interface Executor {
void insert();

void delete();

int update();

Object select(Query query, Class<?> returnType, String tableName, Method method);

List<Object> selectList(Query query, Class<?> aClass, String tableName, Method method);
}
