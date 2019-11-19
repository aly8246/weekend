package com.github.aly8246.core.dispatcher.baseDaoHandler


import com.github.aly8246.core.dispatcher.InitializerDispatcher
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.executor.PageExecutor
import com.github.aly8246.core.executor.SelectExecutor
import com.github.aly8246.core.page.Page
import com.github.aly8246.core.page.PageResult
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.select.SelectExpressionItem
import java.io.StringReader
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

@Suppress("UNCHECKED_CAST")
class BaseDaoDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>) : InitializerDispatcher<T>(proxy, method, args, mongoConnection, target) {
    protected lateinit var originalCommand: String
    protected lateinit var strategySignature: String
    override fun resolverBaseCommand(method: Method): String {
        //根据方法拼接sql
        val contextFactory = BaseDaoContextFactory<T>()
        val strategy = contextFactory.produceStrategy(null, method.name)
        strategySignature = strategy.strategySignature
        return strategy.create(proxy, method, args, mongoConnection, target)
    }

    private fun needOpenPage(): Boolean {
        return this.strategySignature == SelectPageStrategy::class.java.simpleName
    }

    override fun transmitOriginalCommand(originalCommand: String) {
        this.originalCommand = originalCommand
    }

    override fun resolverPrimaryKey(originalCommand: String): String {
        return originalCommand
    }

    override fun run(): T? {
        //从方法的注解中获得sql/或者生成base sql
        baseCommand = this.resolverBaseCommand(method)

        //解析获得参数
        val param = resolverParam(method)

        //获得原始sql(模板替换完成)
        var originalCommand = template(baseCommand, param)

        //解析主键ID
        originalCommand = resolverPrimaryKey(originalCommand)

        //将解析好的sql传递给子类
        this.transmitOriginalCommand(originalCommand)

        //创建statement
        val statement = mongoConnection.createStatement()

        val executorResult: T?
        executorResult = when {
            this.needOpenPage() -> startPage(statement, originalCommand, param)
            else -> selectStatement(statement, originalCommand, param, statement)
        }

        statement.close()

        return executorResult
    }

    private fun startPage(statement: Statement, originalCommand: String, param: MutableMap<Parameter, Any?>): T? {
        var sqlStm = CCJSqlParserManager().parse(StringReader(originalCommand.trim()))
        if (sqlStm !is Select) {
            throw WeekendException("分页异常")
        }
        var plainSelect = sqlStm.selectBody as PlainSelect

        plainSelect.selectItems.removeAll(plainSelect.selectItems)
        plainSelect.selectItems.add(SelectExpressionItem(Column("_id")))

        val pageExecutor = SelectExecutor(plainSelect.toString(), mongoConnection)

        val select = pageExecutor.select(plainSelect.toString())
        val count = select.count()

        println(count)
        //method的0号参数就是page对象
        val page = args!![0] as Page


        val selectStatement = selectStatement(statement, originalCommand, param, statement)

        println(selectStatement)
        //TODO 生成查询数量sql执行

        //得到了总记录数

        //TODO 根据分页条件来再生成带分页条件的sql
        println(page.page)
        println(page.pageSize)
        val pageResult = PageResult<T>()
        pageResult.data = selectStatement as List<T>
        pageResult.page = page.page
        pageResult.pageSize = page.pageSize
        return pageResult as T
    }
}