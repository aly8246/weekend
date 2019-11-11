package com.github.aly8246.core.annotation

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
annotation class WeekendId(val name: Int = 0)
