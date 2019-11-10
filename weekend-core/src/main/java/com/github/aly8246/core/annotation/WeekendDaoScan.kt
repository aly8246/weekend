package com.github.aly8246.core.annotation

import com.github.aly8246.core.proxy.WeekendProxyRegister
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@MustBeDocumented
@Import(WeekendProxyRegister::class)
annotation class WeekendDaoScan(vararg val value: String = [])
