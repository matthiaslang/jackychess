package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.functions.Function;

public class MobFigParams {

    public final Function mobilityMG;
    public final Function mobilityEG;

    public final Function tropismMG;
    public final Function tropismEG;

    public final Function kingAtt;
    public final String propertyMobilityMg;
    public final String propertyMobilityEg;
    public final String propertyKingAtt;
    public final String propertyTropismMg;
    public final String propertyTropismEg;

    public MobFigParams(EvalConfig config, String propBaseName) {
        propertyMobilityMg = propBaseName + "MobMG";
        mobilityMG = config.parseFunction(propertyMobilityMg);
        propertyMobilityEg = propBaseName + "MobEG";
        mobilityEG = config.parseFunction(propertyMobilityEg);

        propertyTropismMg = propBaseName + "TropismMG";
        tropismMG = config.parseFunction(propertyTropismMg);
        propertyTropismEg = propBaseName + "TropismEG";
        tropismEG = config.parseFunction(propertyTropismEg);

        propertyKingAtt = propBaseName + "KingAttack";
        kingAtt = config.parseFunction(propertyKingAtt);
    }
}
