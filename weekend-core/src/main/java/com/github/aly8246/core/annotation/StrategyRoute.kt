package com.github.aly8246.core.annotation

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
annotation class StrategyRoute(val path: String)