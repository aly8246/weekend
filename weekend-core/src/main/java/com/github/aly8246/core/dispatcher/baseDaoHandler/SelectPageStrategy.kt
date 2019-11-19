//package com.github.aly8246.core.dispatcher.baseDaoHandler
//
//import com.github.aly8246.core.driver.MongoConnection
//import com.github.aly8246.core.util.CollectionEntityUtil
//import java.lang.reflect.Method
//
//class SelectPageStrategy<T> : BaseDaoStrategy<T> {
//    override fun createBaseCommand(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>): String {
//        val collectionName = CollectionEntityUtil().collectionName()
//
//        return ""
//    }
//
//}