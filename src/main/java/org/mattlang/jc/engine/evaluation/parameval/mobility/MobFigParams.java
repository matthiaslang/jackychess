package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

@EvalConfigurable
public class MobFigParams {

    @EvalConfigParam(configName = "Mob")
    public MgEgArrayFunction mobility;

    @EvalConfigParam(configName = "Tropism")
    public MgEgArrayFunction tropism;

    @EvalConfigParam(configName = "KingAttack")
    @EvalValueInterval(min = 0, max = 50)
    public MgEgArrayFunction kingAtt;

    public MobFigParams(EvalConfig config) {

    }

}
