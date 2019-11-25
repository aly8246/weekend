package com.github.aly8246.core.dispatcher.baseDaoHandler.base

import com.github.aly8246.core.annotation.BaseMethod
import com.github.aly8246.core.annotation.StrategyRoute
import com.github.aly8246.core.page.Page
import com.github.aly8246.core.page.PageResult
import java.io.Serializable


/**
 * @author 南有乔木
 * 这是一些基础方法！如果你想使用BaseDao。请继续看
 * This is some base method!If you want use BaseDao.Please look!
 *
 *
 *
 * @see com.github.aly8246.core.dispatcher.baseDaoHandler.BaseDaoDispatcher
 */
interface BaseDao<X> {
    /**
     * 这个方法新增一个实体到数据库。只新增不为null的字段，如果没有id则会自动生成25位随机字符
     * This method add one entity to DB.Add only non null fields,If It's haven't primaryKey,I where help you generate 25 random char.
     *
     * @see com.github.aly8246.core.dispatcher.baseDaoHandler.strategy.InsertOneStrategy
     *
     * @param entity 实体类，它不是是空的
     * @param entity entity,Can't be null!
     *
     * @return 返回值总是1或者0,因为它只是新增一个实体类
     * @return Return value is always 1 or 0 ,Because Is't only insert one entity
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun insertOne(entity: X): Int

    /**
     * 这个方法新增很多实体到数据库。如果其中有些实体的字段为null则会为此补全默认类型，其他和[insertOne]差不多
     * This method add entities to DB.If the field of some of these entities is null,the default type will be supplemented!Others explain like [insertOne]
     *
     * @see com.github.aly8246.core.dispatcher.baseDaoHandler.strategy.InsertAllStrategy
     * @param entity 一些实体类，不能为空
     * @param entity entities,Can't be empty!
     *
     * @return 通常它返回了参数的size,如果其中没有重复的主键
     * @return In the ordinary It's return entities.size,If It's haven't duplicate primaryKey
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun insertAll(entity: MutableList<X>): Int

    /**
     * 根据实体类里的主键更新，只更新不为null的字段
     *
     * @param entity 实体类，主键ID不能为空
     * @param entity entity,primaryKey Can't be null
     *
     * @return 返回值总是1或者0,因为它只是新增一个实体类
     * @return Return value is always 1 or 0 ,Because Is't only modify one entity
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun updateById(entity: X): Int

    /**
     * 根据主键删除
     *
     * @param id 主键
     * @param id primaryKey,primaryKey Can't be null
     *
     * @return 返回值总是1或者0,因为它只是删除一个实体类
     * @return Return value is always 1 or 0 ,Because Is't only remove one entity
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun deleteById(id: Serializable): Int

    /**
     * 根据主键删除
     *
     * @param idList 主键
     * @param idList primaryKeys,primaryKeys Can't be null
     *
     * @return 通常它返回了参数的size,如果其中没有重复的主键
     * @return In the ordinary It's return entities.size,If It's haven't duplicate primaryKey
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun deleteByIdIn(idList: MutableList<String>): Int

    /**
     * 根据主键查询单个对象
     *
     * @param id 主键
     * @param id primaryKey,primaryKey Can't be null
     *
     * @return 返回一个对象，如果这个主键存在
     * @return
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectById(id: Serializable): X

    /**
     * 查询全部
     *
     *
     * @return 返回全部集合
     * @return
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectAll(): List<X>

    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectAll(sql: String, vararg param: Any): List<X>

    /**
     * 初始分页,但是我仍然建议你自己实现分页
     * @param page 分页参数类，如果你使用自定义分页，也必须继承自com.github.aly8246.core.page.Page
     * @param sql 自定义sql命令片段
     * @param param 参数合集
     *
     * @return 返回分页查询结果
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @com.github.aly8246.core.annotation.PageMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectPage(page: Page, sql: String, vararg param: Any): PageResult<X>
}