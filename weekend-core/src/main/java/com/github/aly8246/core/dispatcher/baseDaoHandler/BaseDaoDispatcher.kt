package com.github.aly8246.core.dispatcher.baseDaoHandler


import com.github.aly8246.core.annotation.StrategyRoute
import com.github.aly8246.core.dispatcher.InitializerDispatcher
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class BaseDaoDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>) : InitializerDispatcher<T>(proxy, method, args, mongoConnection, target) {
    private lateinit var param: MutableMap<String, Any?>

    protected lateinit var originalCommand: String
    protected lateinit var strategySignature: String
    override fun resolverBaseCommand(method: Method): String {
        //根据方法拼接sql
        val contextFactory = BaseDaoContextFactory<T>()
        val strategyRoute = method.getDeclaredAnnotation(StrategyRoute::class.java)
        val strategy = contextFactory.produceStrategy(strategyRoute.path, method.name)
        strategySignature = strategy.strategySignature
        return strategy.create(proxy, method, args, mongoConnection, target, param)
    }

    override fun transmitOriginalCommand(originalCommand: String) {
        this.originalCommand = originalCommand
    }

    override fun resolverPrimaryKey(originalCommand: String): String {
        return originalCommand
    }

//    override fun run(): T? {
//        //解析获得参数
//        val param = resolverParam(method)
//
//        //从方法的注解中获得sql/或者生成base sql
//        baseCommand = this.resolverBaseCommand(method)
//
//        //获得原始sql(模板替换完成)
//        var originalCommand = template(baseCommand, param)
//
//        //解析主键ID
//        originalCommand = resolverPrimaryKey(originalCommand)
//
//        //将解析好的sql传递给子类
//        this.transmitOriginalCommand(originalCommand)
//
//        //创建statement
//        val statement = mongoConnection.createStatement()
//
//        //获得查询结果
//        val executorResult = selectStatement(statement, originalCommand, param, statement)
//
//        //关闭连接
//        statement.close()
//
//        return executorResult
//    }

    override fun transmitParam(paramMap: MutableMap<String, Any?>) {
        this.param = paramMap
    }
}