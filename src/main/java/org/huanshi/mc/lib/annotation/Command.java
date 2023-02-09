package org.huanshi.mc.lib.annotation;

import org.huanshi.mc.lib.command.Environment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Command {
    Environment environment() default Environment.ALL;
    boolean op() default false;
    boolean combating() default true;
    String permission() default "";
    String head();
    String[] args() default {};
}