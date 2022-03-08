package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.functions.KingAttackFun;
import org.mattlang.jc.engine.evaluation.parameval.functions.MobLinFun;
import org.mattlang.jc.engine.evaluation.parameval.functions.TropismFun;

public class MobFigParams {

    public final MobLinFun mobilityMG;
    public final MobLinFun mobilityEG;

    public final TropismFun tropismMG;
    public final TropismFun tropismEG;

    public final KingAttackFun kingAtt;

    public MobFigParams(EvalConfig config, String propBaseName) {
        mobilityMG = config.parseFun(propBaseName + "MobMG");
        mobilityEG = config.parseFun(propBaseName + "MobEG");

        tropismMG = config.parseTrFun(propBaseName + "TropismMG");
        tropismEG = config.parseTrFun(propBaseName + "TropismEG");

        kingAtt = config.parseKAFun(propBaseName + "KingAttack");
    }
}
