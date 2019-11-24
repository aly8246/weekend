package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import java.lang.reflect.Method

/**
 * @see com.github.aly8246.core.dispatcher.baseDaoHandler.base.BaseDao.selectAll
 */
class SelectAllStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>, param: MutableMap<String, Any?>): String {
        val collectionName = this.collectionName(target)

        //如果是带参数
        val sql = args?.get(0)
        if (sql != null) {
            throw WeekendException("暂时还不能解析带参数的SelectAll")
//            //如果sql不为空，此策略还要负责自己解析参数
//            //TODO
//
//            println(args[1])
//            //如果是
//            return "select * from $collectionName $sql"
        }
        return "select * from $collectionName"
    }
}