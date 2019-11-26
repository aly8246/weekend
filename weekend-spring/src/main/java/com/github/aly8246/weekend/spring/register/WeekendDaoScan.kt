package com.github.aly8246.weekend.spring.register

import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@MustBeDocumented
@Import(WeekendDaoBatchRegister::class)
annotation class WeekendDaoScan(vararg val value: String = [])
