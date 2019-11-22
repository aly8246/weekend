package com.github.aly8246.core.dispatcher.baseDaoHandler.strategy

import com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoStrategy
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

class UpdateByIdStrategy<T> : CollectionEntityResolver(), BaseDaoStrategy<T> {
    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
        val collectionName = this.collectionName(target)
        val primaryKeyName = this.primaryKeyName(target)
        //TODO 获取所有不为null的字段，
        var updateField = "set `name` = 100 set `age` = 80"

        //TODO 注意区分是字符串还是数字的id
        var idValue = "'1234567'"
        var idValue2 = "1234567"
        return "update $collectionName $updateField where $primaryKeyName = $idValue"
    }
}