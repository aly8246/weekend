package com.github.aly8246.core.exec;

import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractExecutor implements Executor {
    @Override
    public void insert() {

    }

    @Override
    public void delete() {

    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public Object select(Query query, Class<?> returnType, String tableName, Method method) {
        return null;
    }

    @Override
    public List<Object> selectList(Query query, Class<?> aClass, String tableName, Method method) {
        return null;
    }
}
