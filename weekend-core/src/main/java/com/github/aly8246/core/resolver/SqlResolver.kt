package com.github.aly8246.core.resolver

interface SqlResolver {
    fun resolver(sql: String): SqlResult
}