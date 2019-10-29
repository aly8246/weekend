package com.github.aly8246.core.dispatcher;

import com.github.aly8246.core.exec.SelectExecutor;
import com.github.aly8246.core.handler.Operation;
import com.github.aly8246.core.util.Print;
import com.github.aly8246.core.util.PrintImpl;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;


public class SimpleDispatcher<T> extends AbstractDispatcher<T> {

public SimpleDispatcher(Object proxy, Method method, Object[] args, RetClass retClass) {
	super(proxy, method, args, retClass);
}

@Override
public Object exec() {
	new PrintImpl().debug("执行普通字段调度器");
	return super.exec();
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
					return (T) new SelectExecutor().select(query, this.retClass.clazz(), operation.getTableName(), method);
				case COLLECTION:
					System.out.println("查询列表");
					return (T) new SelectExecutor().selectList(query, this.retClass.clazz(), operation.getTableName(), method);
				case PAGE:
					System.out.println("可能需要分页，但是现在还没有处理");
				case NULL:
					System.out.println("警告，查询结果为空，不做任何处理，也不执行查询");
					return null;
				default:
					throw new RuntimeException("暂时无法处理");
			}
		}
		case OTHER:
		default:
	}
	return null;
}


}
