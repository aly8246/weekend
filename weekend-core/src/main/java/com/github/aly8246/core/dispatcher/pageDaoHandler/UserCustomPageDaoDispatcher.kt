package com.github.aly8246.core.dispatcher.pageDaoHandler


import com.github.aly8246.core.driver.MongoConnection
import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class UserCustomPageDaoDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>) : PageDaoDispatcher<T>(proxy, method, args, mongoConnection, target) {
    override fun resolverBaseCommand(method: Method): String {
        return super.resolverBaseCommand(method)
    }

    override fun transmitOriginalCommand(originalCommand: String) {
        super.transmitOriginalCommand(originalCommand)
    }

    override fun resolverPrimaryKey(originalCommand: String): String {
        return super.resolverPrimaryKey(originalCommand)
    }

    override fun run(): T? {
        return super.run()
    }
}