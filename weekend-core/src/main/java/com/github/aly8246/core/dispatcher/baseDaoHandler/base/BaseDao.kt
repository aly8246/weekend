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
     * TODO
     * 这个方法新增一个实体到数据库。只新增不为null的字段，如果没有id则会自动生成25位随机字符
     * This method add one entity to DB.Add only non null fields,If It's haven't primaryKey,I where help you generate 25 random char.
     *
     * @see com.github.aly8246.core.dispatcher.baseDaoHandler.strategy.InsertSelectiveStrategy
     *
     * @param entity 实体类，它不是是空的
     * @param entity entity,Can't be null!
     *
     * @return  返回值总是1或者0,因为它只是新增一个实体类
     * @return  Return value is always 1 or 0 ,Because Is't only insert one entity
     *
     * @exception NullPointerException
     */
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun insertSelective(entity: X): Int

    /**
     * TODO
     * 这个方法新增很多实体到数据库。如果其中有些实体的字段为null则会为此补全默认类型，其他和[insertSelective]差不多
     * This method add entities to DB.If the field of some of these entities is null,the default type will be supplemented!Others explain like [insertSelective]
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
     * TODO
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

    //TODO
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun deleteById(id: Serializable): Int

    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectById(id: Serializable): X

    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectAll(): List<X>

    //TODO
    @BaseMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectAll(sql: String, vararg param: Any): List<X>

    @BaseMethod
    @com.github.aly8246.core.annotation.PageMethod
    @StrategyRoute("com.github.aly8246.core.dispatcher.baseDaoHandler.strategy")
    fun selectPage(page: Page, sql: String, vararg param: Any): PageResult<X>

}