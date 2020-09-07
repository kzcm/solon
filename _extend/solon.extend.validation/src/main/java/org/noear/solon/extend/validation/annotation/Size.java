package org.noear.solon.extend.validation.annotation;

import org.noear.solon.annotation.XNote;

import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {
    /**
     * param names
     * */
    @XNote("param names")
    String[] value();

    int min();
    int max();

    String message() default "";
}