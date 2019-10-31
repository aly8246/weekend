package com.github.aly8246.core.annotation

import com.github.aly8246.core.handler.Condition
import com.github.aly8246.core.handler.SqlConditionHandler
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
annotation class Command(vararg val value: String, val handler: KClass<out Condition> = SqlConditionHandler::class, val returnType: KClass<*> = Collection::class)
