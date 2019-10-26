package com.github.aly8246.core.annotation;

import com.github.aly8246.core.handler.Condition;
import com.github.aly8246.core.handler.SqlConditionHandler;

import java.lang.annotation.*;
import java.util.Collection;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Exec {
    String[] value();

    Class<? extends Condition> handler() default SqlConditionHandler.class;

    Class<?> returnType() default Collection.class;
}
