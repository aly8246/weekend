package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

/**
 * @see com.github.aly8246.core.dispatcher.baseDaoHandler.base.BaseDao.insertAll
 */
class InsertAllStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>, param: MutableMap<String, Any?>): String {
        val collectionName = this.collectionName(target)

        //实体集合
        val entityList = args!![0] as MutableList<*>

        //实体不为空的字段和值
        val resolverEntity: MutableList<MutableMap<String, Any>> = mutableListOf()
        for (entity in entityList) {
            if (entity != null) {
                resolverEntity.add(this.resolverEntity<T>(entity))
            }
        }

        //值 (1,2,3),(4,5,6)
        val entityValueList: MutableList<String> = mutableListOf()
        resolverEntity.forEach { e ->
            run {
                entityValueList.add("(${e.values.joinToString(",")})")
            }
        }

        return "INSERT INTO $collectionName (${resolverEntity[0].keys.joinToString(",")}) values ${entityValueList.joinToString(",")}"
    }
}