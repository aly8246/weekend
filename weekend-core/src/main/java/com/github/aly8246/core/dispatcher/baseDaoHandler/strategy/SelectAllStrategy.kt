package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

class SelectAllStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
        val collectionName = this.collectionName(target)
        return "select * from $collectionName"
    }
}