package org.mattlang.jc.engine.evaluation.annotation.configure;

import lombok.Getter;

/**
 * Contains the effective extracted configuration definition of a config annotation.
 */
@Getter
public class MgEgConfigDefinition {

    private final ConfigDefinition configDefinition;

    private final String fullQualifiedPropNameMg;
    private final String fullQualifiedPropNameEg;

    public MgEgConfigDefinition(ConfigDefinition configDefinition) {
        this.configDefinition = configDefinition;

        fullQualifiedPropNameMg = configDefinition.getFullQualifiedConfigName() + "MG";
        fullQualifiedPropNameEg = configDefinition.getFullQualifiedConfigName() + "EG";
    }
}
