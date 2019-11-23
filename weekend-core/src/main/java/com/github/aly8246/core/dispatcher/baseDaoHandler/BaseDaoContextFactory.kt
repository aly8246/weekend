package com.github.aly8246.core.dispatcher.baseDaoHandler

import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.util.WordUtil
import java.lang.StringBuilder

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/19 上午 11:36
 * @description：
 * @version:   ：V
 */
class BaseDaoContextFactory<T> {
    //每个base方法都需要解析出sql，如果不使用策略模式，则会有一大堆if else方法
    //需要定义一个接口，通过实现接口来最终获得完整的sql(还未处理模板参数)

    //实现逻辑::
    //step1. 如果是selectById
    //step2. select * from [根据实体解析表名] where [根据实体获得WeekendId，没有就找id/_id] = #{用户输入的参数}

    //step1. 如果是page分页-没有参数
    //step2. select count(1) from [根据实体解析表名]
    //step3. select * from [根据实体解析表名] limit [根据page解析skip],[根据page解析limit]
    //
    // step1. 如果是page分页-有参数
    //step2. select count(1) from [根据实体解析表名] [拼接用户传递的sql参数]
    //step3. select * from [根据实体解析表名] [拼接用户传递的sql参数] limit [根据page解析skip],[根据page解析limit]

    //实现步骤:
    //step1. 根据方法名称，通过策略器选择对应的实现方法
    //step2. 调用实现方法的处理方法
    fun produceStrategy(strategyPath: String?, methodName: String): BaseCommandContext<T> {
        val classPath = StringBuilder()

        //从方法上获取策略的位置
        classPath.append(strategyPath)
        classPath.append(".")
        classPath.append(WordUtil.toUpperCaseFirstOne(methodName))
        classPath.append("Strategy")

        val strategyInstance: Any?
        try {
            strategyInstance = Class.forName(classPath.toString()).newInstance()
        } catch (e: Exception) {
            throw WeekendException("还未实现的BaseDao方法:$methodName 或无法使用的策略:${e.message}")
        }

        return BaseCommandContext(strategyInstance as BaseDaoStrategy<T>)
    }
}