package org.mattlang.jc.engine.evaluation.annotation.configure;

import java.lang.reflect.Field;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.MirrorPattern;

import lombok.Getter;

/**
 * Contains the effective extracted configuration definition of a config annotation.
 */
@Getter
public class ConfigDefinition {

    private final String declaredFieldName;
    private final String effectiveConfigName;
    private final String fullQualifiedConfigName;
    private final String definedPrefix;
    private final boolean disableTuning;
    private final Class<?> declaredFieldType;

    private final boolean mirror;

    public ConfigDefinition(PrefixStack prefixes, EvalConfigParam param,
            MirrorPattern mirrorPattern,
            Field declaredField) {
        declaredFieldName = declaredField.getName();
        declaredFieldType = declaredField.getType();
        definedPrefix = param.prefix();
        effectiveConfigName = !"".equals(param.name()) ? param.name() : declaredFieldName;

        disableTuning = param.disableTuning();

        fullQualifiedConfigName = prefixes.with(definedPrefix).with(effectiveConfigName).getQualifiedName();

        mirror = mirrorPattern != null ? mirrorPattern.mirror() : true;
    }
}
