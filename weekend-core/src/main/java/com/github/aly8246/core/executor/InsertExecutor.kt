package com.github.aly8246.core.executor

import com.github.aly8246.core.driver.MongoConnection
import net.sf.jsqlparser.statement.insert.Insert

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/15 上午 11:54
 * @description：
 * @version:   ：V
 */
class InsertExecutor(sql: String) : AbstractExecutor(sql), Executor {
    override fun insert(sql: String, mongoConnection: MongoConnection): Int {
        val insert = this.statement as Insert

        println("要执行的sql:${sql}")
        println("假设sql执行成功")
        return 1
    }
}