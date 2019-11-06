package com.github.aly8246.core.annotation


import com.github.aly8246.core.resolver.CommandResolver
import com.github.aly8246.core.resolver.mysql.MySqlCommandResolver
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
annotation class Command(vararg val value: String, val handler: KClass<out CommandResolver> = MySqlCommandResolver::class, val returnType: KClass<*> = Collection::class)
