package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import com.mongodb.client.MongoCursor
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.statement.Statement
import org.bson.Document
import java.io.StringReader

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/15 上午 11:52
 * @description：
 * @version:   ：V
 */
abstract class AbstractExecutor(sql: String) : Executor {
    protected var statement: Statement = CCJSqlParserManager().parse(StringReader(sql.trim()))
    override fun select(sql: String, mongoConnection: MongoConnection): MongoCursor<Document> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(sql: String, mongoConnection: MongoConnection): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(sql: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(sql: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}