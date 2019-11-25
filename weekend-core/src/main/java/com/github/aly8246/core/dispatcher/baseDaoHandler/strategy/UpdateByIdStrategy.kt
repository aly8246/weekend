package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

/**
 * @see com.github.aly8246.core.dispatcher.baseDaoHandler.base.BaseDao.updateById
 */
class UpdateByIdStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>, param: MutableMap<String, Any?>): String {
        val collectionName = this.collectionName(target)
        val primaryKeyName = this.primaryKeyName(target)

        //第0个参数一定是实体类
        val resolverEntity = this.resolverEntity<T>(args!![0])
        //获取id的值
        val primaryKeyValue = this.resolverPrimaryKeyValue<T>(args[0])

        //所有不为null的字段
        val updateField = StringBuffer()
        for (entityEntry in resolverEntity) {
            when (entityEntry.value) {
                is String -> updateField.append("set `${entityEntry.key}` = ' ${entityEntry.value}'")
                else -> updateField.append("set `${entityEntry.key}` = ${entityEntry.value}")
            }
        }

        return "update $collectionName $updateField where $primaryKeyName = $primaryKeyValue"
    }
}