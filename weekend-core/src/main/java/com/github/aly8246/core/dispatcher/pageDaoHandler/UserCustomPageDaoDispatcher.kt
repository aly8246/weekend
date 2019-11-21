package com.github.aly8246.core.dispatcher.pageDaoHandler


import com.github.aly8246.core.annotation.Command
import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.page.Page
import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class UserCustomPageDaoDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, mongoConnection: MongoConnection, target: Class<T>) : PageDaoDispatcher<T>(proxy, method, args, mongoConnection, target) {
    protected lateinit var commandAnnotation: Command

    //当用户的分页方法存在@PageMethod注解的时候，并且没有@BaseMethod的时候则是用户自定义分页
    //用户传递一个普通方法。
    //重写resolverBaseCommand方法即可
    override fun resolverBaseCommand(method: Method): String {
        try {
            commandAnnotation = method.getDeclaredAnnotation(Command::class.java)
        } catch (e: IllegalStateException) {
            throw WeekendException("自定义分页必须要sql")
        }
        return commandAnnotation.value.joinToString("")
    }


    //如果用户自定义分页，则有可能在任意位置传递page参数
    //父类基础分页从固定第一位获取，因为是我写的第一位所以固定第一位，这里需要重写获取位置
    override fun pageParam(): Page {
        //TODO 从任意位置获取page参数
        try {
            return args!![0] as Page
        } catch (e: Exception) {
            throw WeekendException("未找到任何分页参数")
        }

    }

}