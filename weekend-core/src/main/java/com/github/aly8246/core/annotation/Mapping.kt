package com.github.aly8246.core.annotation

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
annotation class Mapping(
        vararg val name: String = [],
        val type: KClass<*> = String::class
)
