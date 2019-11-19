package com.github.aly8246.core.dispatcher.baseDaoHandler

import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

class SelectPageStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
        val collectionName = this.collectionName(target)

        //TODO 如果用户有查询条件，在此加上
        return "select * from $collectionName"
    }
}