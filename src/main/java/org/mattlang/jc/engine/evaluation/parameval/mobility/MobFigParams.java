package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.functions.Function;

public class MobFigParams {

    public final Function mobilityMG;
    public final Function mobilityEG;

    public final Function tropismMG;
    public final Function tropismEG;

    public final Function kingAtt;

    public MobFigParams(EvalConfig config, String propBaseName) {
        mobilityMG = config.parseFunction(propBaseName + "MobMG");
        mobilityEG = config.parseFunction(propBaseName + "MobEG");

        tropismMG = config.parseFunction(propBaseName + "TropismMG");
        tropismEG = config.parseFunction(propBaseName + "TropismEG");

        kingAtt = config.parseFunction(propBaseName + "KingAttack");
    }
}
