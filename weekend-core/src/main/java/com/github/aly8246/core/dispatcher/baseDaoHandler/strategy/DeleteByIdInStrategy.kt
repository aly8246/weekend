package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

/**
 * @see com.github.aly8246.core.dispatcher.baseDaoHandler.base.BaseDao.deleteByIdIn
 */
class DeleteByIdInStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>, param: MutableMap<String, Any?>): String {
        val collectionName = this.collectionName(target)
        val primaryKeyName = this.primaryKeyName(target)
        val primaryKeyValueList = args!![0] as MutableList<*>
        return "delete from $collectionName where $primaryKeyName = (${primaryKeyValueList.joinToString(",")})"
    }
}