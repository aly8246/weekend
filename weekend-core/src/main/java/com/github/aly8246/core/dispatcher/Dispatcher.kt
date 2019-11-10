package com.github.aly8246.core.dispatcher

interface Dispatcher<T> {
    fun execute(): T?
}