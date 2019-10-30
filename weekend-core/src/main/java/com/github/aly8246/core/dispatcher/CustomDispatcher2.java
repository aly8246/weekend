package com.github.aly8246.core.dispatcher;

import com.github.aly8246.core.annotation.Command;
import com.github.aly8246.core.exec.CustomExecutor;
import com.github.aly8246.core.handler.Condition;
import com.github.aly8246.core.handler.Operation;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;

public class CustomDispatcher2<T> extends AbstractDispatcher2<T> {
    public CustomDispatcher2(Object proxy, Method method, Object[] args, RetClass retClass) {
        super(proxy, method, args, retClass);
    }

    @Override
    public Object execute() {
        System.out.println("自定义字段调度器执行");
        return super.execute();
    }

    @Override
    protected T switchExecutor(Operation operation, Query query, Method method) {
        switch (operation.getOperation()) {
            case INSERT:

            case DELETE:

            case UPDATE:

            case SELECT: {
                switch (this.retClass.classType()) {
                    case OBJECT:
                        System.out.println("查询单个");
                        return (T) new CustomExecutor().select(query, this.retClass.clazz(), operation.getTableName(), method);
                    case COLLECTION:
                        return (T) new CustomExecutor().selectList(query, this.retClass.clazz(), operation.getTableName(), method);
                    case PAGE:
                        System.out.println("可能需要分页，但是现在还没有处理");
                    case NULL:
                        System.out.println("警告，查询结果为空，不做任何处理，也不执行查询");
                }
            }
            case OTHER:
            default:
                throw new RuntimeException("暂时无法处理");
        }
    }

    @Override
    protected void resolveExec(Method method) {
        super.resolveExec(method);
    }

    @Override
    protected String completeCommand(Command command) {
        return super.completeCommand(command);
    }

    @Override
    protected Operation buildCondition(Command command, String baseCommand) {
        return super.buildCondition(command, baseCommand);
    }

    @Override
    protected Condition getConditionHandler(Command command) {
        return super.getConditionHandler(command);
    }
}
