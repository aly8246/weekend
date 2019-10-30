package com.github.aly8246.core.annotation;

import com.github.aly8246.core.proxy.WeekendProxyRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(WeekendProxyRegister.class)
public @interface WeekendDaoScan {
String[] value() default {};
}
