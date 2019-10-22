package com.github.aly8246.core.annotation;

import com.github.aly8246.core.handler.Command;
import com.github.aly8246.core.handler.SqlCommandHandler;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Exec {
String[] value();

Class<? extends Command> handler() default SqlCommandHandler.class;

}
