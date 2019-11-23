package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.StringBuilder
import java.lang.reflect.Method

class InsertAllStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
        val collectionName = this.collectionName(target)

        val entityList = args!![0] as MutableList<*>
        val resolverEntity: MutableList<MutableMap<String, Any>> = mutableListOf()
        for (entity in entityList) {
            if (entity != null) {
                resolverEntity.add(this.resolverEntity<T>(entity))
            }
        }

        val entityValueList: MutableList<String> = mutableListOf()
        resolverEntity.forEach { e ->
            run {
                entityValueList.add("(${e.values.joinToString(",")})")
            }
        }
        return "INSERT INTO $collectionName (${resolverEntity[0].keys.joinToString(",")}) values ${entityValueList.joinToString(",")}"
    }
}