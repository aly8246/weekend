package com.github.aly8246.core.exec;

import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.List;

public class BaseExecutor extends AbstractExecutor {
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
        return super.select(query, returnType, tableName, method);
    }

    @Override
    public List<Object> selectList(Query query, Class<?> aClass, String tableName, Method method) {
        return super.selectList(query, aClass, tableName, method);
    }

}
