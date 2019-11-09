package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA", "UNCHECKED_CAST")
class OriginalDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection) : InitializerDispatcher<T>(proxy, method, args,mongoConnection) {

}