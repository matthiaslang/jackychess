package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.Function;

public class MobFigParams {

    public final ArrayFunction mobilityMG;
    public final ArrayFunction mobilityEG;

    public ArrayFunction mobilityMGEG;

    public final Function tropismMG;
    public final Function tropismEG;

    public final ArrayFunction kingAttMg;
    public final ArrayFunction kingAttEg;
    public ArrayFunction kingAttMgEg;
    public final String propertyMobilityMg;
    public final String propertyMobilityEg;
    public final String propertyKingAttMg;
    public final String propertyKingAttEg;
    public final String propertyTropismMg;
    public final String propertyTropismEg;

    public MobFigParams(EvalConfig config, String propBaseName) {
        propertyMobilityMg = propBaseName + "MobMG";
        mobilityMG = config.parseArray(propertyMobilityMg);
        propertyMobilityEg = propBaseName + "MobEG";
        mobilityEG = config.parseArray(propertyMobilityEg);

        propertyTropismMg = propBaseName + "TropismMG";
        tropismMG = config.parseFunction(propertyTropismMg);
        propertyTropismEg = propBaseName + "TropismEG";
        tropismEG = config.parseFunction(propertyTropismEg);

        // todo mg/eg split
        propertyKingAttMg = propBaseName + "KingAttackMg";
        kingAttMg = config.parseArray(propertyKingAttMg);

        propertyKingAttEg = propBaseName + "KingAttackEg";
        kingAttEg = config.parseArray(propertyKingAttEg);

        updateCombinedVals();
    }

    public void updateCombinedVals() {
        mobilityMGEG = ArrayFunction.combine(mobilityEG, mobilityMG);
        kingAttMgEg = ArrayFunction.combine(kingAttEg, kingAttMg);
    }
}
