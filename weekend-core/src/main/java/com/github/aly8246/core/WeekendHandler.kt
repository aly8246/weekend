package com.github.aly8246.core

abstract class WeekendHandler {
    //  lateinit var preHandler: WeekendHandler
    lateinit var nextHandler: WeekendHandler

    open fun handler() = this.nextHandler.handle()

    abstract fun handle()
}