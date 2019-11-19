package com.github.aly8246.core.dispatcher.baseDaoHandler

import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

open class SelectByIdStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
        //获取集合名称
        val collectionName = this.collectionName(target)

        //获取主键名称
        val primaryKeyName = this.primaryKeyName(target)

        return "select * from $collectionName where $primaryKeyName = '${args!![0]}'"
    }
}