package org.mattlang.jc.engine.evaluation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an additional prefix name part.
 * the prefixes are collected on the path from the root parameterized evaluation down to the respective config parameter.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.FIELD})
public @interface EvalConfigPrefix {
    public String prefix();
}