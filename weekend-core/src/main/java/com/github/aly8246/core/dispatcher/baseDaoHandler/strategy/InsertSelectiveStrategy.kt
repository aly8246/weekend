package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

class InsertSelectiveStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
        val collectionName = this.collectionName(target)
        //TODO 获取所有不为null的字段，
        var insertField = "(`id`)"
        var insertValue = "(`1234567`)"
        return "INSERT INTO $collectionName $insertField values $insertValue"
    }
}