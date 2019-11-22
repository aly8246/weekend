package com.github.aly8246.core.dispatcher.pageDaoHandler


import com.github.aly8246.core.dispatcher.InitializerDispatcher
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
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

@Suppress("UNCHECKED_CAST")
open class PageDaoDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>) : InitializerDispatcher<T>(proxy, method, args, mongoConnection, target) {
    protected lateinit var originalCommand: String
    protected lateinit var strategySignature: String
    override fun resolverBaseCommand(method: Method): String {
        return ResolverBaseCommand(method, args, target).resolverBaseCommand()
    }

    open class ResolverBaseCommand<T>(var method: Method, var args: Array<Any>?, var target: Class<T>) : CollectionEntityResolver() {

        fun resolverBaseCommand(): String {
            val collectionName = this.collectionName(this.target)

            if (args?.get(1) != null) return "select * from $collectionName ${args?.get(1)}"
            return "select * from $collectionName "
        }
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

        return startPage(originalCommand, param)
    }

    private fun startPage(originalCommand: String, param: MutableMap<String, Any?>): T? {
        val statement = mongoConnection.createStatement()

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


        val page = this.pageParam()

        /**
         * @Description mysql分页转mongodb分页
         * skip: page*pageSize-pageSize
         * limit: pageSize-pageSize
         */
        val pageSql = "$originalCommand limit ${page.page.times(page.pageSize) - page.pageSize},${page.pageSize}"
        val selectStatement = selectStatement(statement, pageSql, param, statement)

        statement.close()

        val pageResult = PageResult<T>()
            if (selectStatement != null)
            pageResult.data = selectStatement as List<T>
        pageResult.page = page.page
        pageResult.total = count
        pageResult.totalPage = (count + page.pageSize - 1) / page.pageSize
        pageResult.pageSize = page.pageSize
        return pageResult as T
    }

    /**
     * @see com.github.aly8246.core.base.BaseDao.selectPage
     */
    //method的0号参数就一定是是page对象
    override fun pageParam(): Page {
        return args!![0] as Page
    }
    /**
     * @Description 分页算法-倒推法:根据结束index来推算起始index
     * @Author 南有乔木
     * @Email 1558146696@qq.com
     * @Date 2019/06/13 上午 11:55
     * @Infer 1
     * 总数 21条
     * 页index  页大小  理想结果          结束index--页index*页大小:     起始index--页index*页大小-页大小+1
     * 4       5      16-20               4 *5 =20                        20-5 +1=16
     * 2       3      4 -6                2 *3 =6                         6 -3 +1=4
     * 1       10     1 -10               1 *10=10                        10-10+1=1
     * 2       10     11-20               2 *10=20                        20-10+1=11
     * 5       4      17-20               5 *4 =20                        20-4 +1=17
     * 1       1      1 -1                1 *1 =1                         1*1-1+1
     */
    /**
     * @Description mysql分页转mongodb分页
     * skip: page*pageSize-pageSize
     * limit: pageSize-pageSize
     */
}