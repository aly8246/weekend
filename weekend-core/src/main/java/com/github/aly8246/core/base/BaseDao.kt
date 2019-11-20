package com.github.aly8246.core.base

import com.github.aly8246.core.annotation.BaseMethod
import com.github.aly8246.core.page.Page
import com.github.aly8246.core.page.PageResult
import java.io.Serializable
import java.util.*


interface BaseDao<X> {

    @BaseMethod
    fun selectById(id: Serializable): X

    @BaseMethod
    fun insert(): Int

    @BaseMethod
    fun selectAll(): List<X>

    @BaseMethod
    fun selectAll(sql: String, queryObject: Any): List<X>

    @BaseMethod
    @com.github.aly8246.core.annotation.Page
    fun selectPage(page: Page, sql: String, entity: Any): PageResult<X>

    @BaseMethod
    @com.github.aly8246.core.annotation.Page
    fun selectPage(page: Page, sql: String, vararg param: Any): PageResult<X>

}