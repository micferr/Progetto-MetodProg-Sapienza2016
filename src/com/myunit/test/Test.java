package com.myunit.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Test {
    class None extends Throwable {
        private None() {}
    }

    Class<? extends Throwable> expected() default None.class;
    boolean optional() default false;
}
