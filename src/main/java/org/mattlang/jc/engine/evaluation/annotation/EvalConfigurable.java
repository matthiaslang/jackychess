package org.mattlang.jc.engine.evaluation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class to have additional config parameter.
 * Used it recursively iterate inner classes of the evaluation function class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EvalConfigurable {
    public String prefix() default "";
}