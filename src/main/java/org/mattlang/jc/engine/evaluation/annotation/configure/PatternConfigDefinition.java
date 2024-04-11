package org.mattlang.jc.engine.evaluation.annotation.configure;

import lombok.Getter;

@Getter
public class PatternConfigDefinition {

    private final MgEgConfigDefinition mgEgConfigDefinition;
    private final String subDir;
    private final String csvTableNameMG;
    private final String csvTableNameEG;
    private final String csvTableName;

    private final String csvTablePathNameMg;
    private final String csvTablePathNameEg;
    private final String csvTablePathName;

    public PatternConfigDefinition(MgEgConfigDefinition mgEgConfigDefinition, PrefixStack prefixes) {
        this.mgEgConfigDefinition = mgEgConfigDefinition;
        subDir = prefixes.with(mgEgConfigDefinition.getConfigDefinition().getDefinedPrefix()).getQualifiedPathName();
        String effectiveConfigName = mgEgConfigDefinition.getConfigDefinition().getEffectiveConfigName();
        csvTableNameMG = effectiveConfigName + "MG" + ".csv";
        csvTableNameEG = effectiveConfigName + "EG" + ".csv";
        csvTableName = effectiveConfigName + ".csv";

        String qualifiedPathName = prefixes
                .with(mgEgConfigDefinition.getConfigDefinition().getDefinedPrefix())
                .with(effectiveConfigName)
                .getQualifiedPathName();
        csvTablePathNameMg = qualifiedPathName + "MG" + ".csv";
        csvTablePathNameEg = qualifiedPathName + "EG" + ".csv";
        csvTablePathName = qualifiedPathName + ".csv";

    }
}
