package com.github.aly8246.core.dispatcher.baseDaoHandler

import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/19 上午 11:11
 * @description：
 * @version:   ：V
 */
interface BaseDaoStrategy<T> {
    fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String
}