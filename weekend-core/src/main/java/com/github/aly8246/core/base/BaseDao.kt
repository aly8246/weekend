package com.github.aly8246.core.base

import com.github.aly8246.core.annotation.BaseMethod
import java.io.Serializable


interface BaseDao<T> {

    @BaseMethod
    fun selectById(id: Serializable): T

    @BaseMethod
    fun insert(): Int

    @BaseMethod
    fun selectAll(): List<T>

    @BaseMethod
    fun selectAll(sql: String): T

    @BaseMethod
    fun selectPage(page: Page): PageResult<T>

    @BaseMethod
    fun selectPage(page: Page, sql: String): PageResult<T>


}