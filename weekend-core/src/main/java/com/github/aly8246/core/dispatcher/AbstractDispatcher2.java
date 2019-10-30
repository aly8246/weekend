package com.github.aly8246.core.dispatcher;

import com.github.aly8246.core.annotation.Command;
import com.github.aly8246.core.handler.Condition;
import com.github.aly8246.core.handler.Operation;
import com.github.aly8246.core.handler.SqlConditionHandler;
import com.github.aly8246.core.query.QueryRunner;
import com.github.aly8246.core.template.BaseTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:20
 * @description：
 * @version: ：V
 */
public abstract class AbstractDispatcher2<T> implements Dispatcher {
    protected Object proxy;
    protected Method method;
    protected Object[] args;
    protected RetClass retClass;
    protected Command command;

    public AbstractDispatcher2(Object proxy, Method method, Object[] args, RetClass retClass) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.retClass = retClass;
    }

//解析查询注解
//参数+命令  ->  基础命令
//基础命令   ->  执行条件类
//执行条件类 ->  Query对象
//Query对象 ->  获得最终执行结果

    @Override
    public Object execute() {
        //step1. 处理Exec注解
        this.resolveExec(this.method);

        //step2. 将模板语法里的模板参数替换成实参,得到 =基础命令=
        String baseCommand = this.completeCommand(this.command);

        //step3. 根据 =基础命令= 来解析 >执行条件类<
        Operation operation = this.buildCondition(this.command, baseCommand);

        //step4. 将 >执行条件类< 转换为最终执行需要的Query对象
        Query query = new QueryRunner().run(operation.getConditionsList());

        //step5. 将 Query 交给最终的执行器
        return this.switchExecutor(operation, query, method);
    }

    protected abstract T switchExecutor(Operation operation, Query query, Method method);


    protected void resolveExec(Method method) {
        Command command = method.getDeclaredAnnotation(Command.class);
        if (command == null) throw new RuntimeException("必须为weekend的接口方法指定SQL");
        this.command = command;
    }


    protected String completeCommand(Command command) {
        if (command == null) throw new RuntimeException("接手注解管理必须处理结果或者调用父类方法");
        return new BaseTemplate().completeCommand(command);
    }

    protected Operation buildCondition(Command command, String baseCommand) {
        //step1. 获取条件处理器
        Condition conditionHandler = this.getConditionHandler(command);

        //step2. 调用条件处理器来处理命令，并且得到执行条件
        return conditionHandler.run(baseCommand);
    }


    protected static String regxListParamClass(String source) {
        Matcher matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source);
        for (; matcher.find(); ) {
            return matcher.group();
        }
        throw new RuntimeException("异常regxListParamClass");
    }

    protected Condition getConditionHandler(Command command) {
        Class<? extends Condition> handler = command.handler();
        try {
            return handler.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new SqlConditionHandler();
    }
}
