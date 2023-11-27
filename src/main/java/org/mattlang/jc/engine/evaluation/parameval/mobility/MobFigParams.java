package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

public class MobFigParams {

    public MgEgArrayFunction mobility;
    public MgEgArrayFunction tropism;
    public final MgEgArrayFunction kingAtt;

    public MobFigParams(EvalConfig config, String propBaseName) {
        kingAtt = new MgEgArrayFunction(config, propBaseName + "KingAttack");
        tropism = new MgEgArrayFunction(config, propBaseName + "Tropism", "MG", "EG");
        mobility = new MgEgArrayFunction(config, propBaseName + "Mob", "MG", "EG");

    }

}
