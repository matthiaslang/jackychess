package org.mattlang.jc.engine.evaluation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set instructions to mirror a pattern
 * for tuning. Default is that patterns are mirrored to safe time (as we have effectively only to tune half the values)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface MirrorPattern {

    /**
     * Default is to mirror patterns. With this parameter mirroring can be disabled.
     *
     * @return
     */
    public boolean mirror() default true;
}