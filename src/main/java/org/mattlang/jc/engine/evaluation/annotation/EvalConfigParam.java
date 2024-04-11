package org.mattlang.jc.engine.evaluation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark an evaluation parameter. It describes its config name, its relative prefix name part
 * and if it is mg/eg combined.
 * It can be marked to disable it for tuning.
 * The information is used to derive the full configuration name in the property file (or path of a csv file)
 * and as marker to initialize this value from the configuration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface EvalConfigParam {

    /**
     * Config Name of the configuration parameter.
     * If not set, the name of the attribute is used instead.
     *
     * @return
     */
    public String name() default "";

    public String prefix() default "";

    public boolean mgEgCombined() default false;

    public boolean disableTuning() default false;
}