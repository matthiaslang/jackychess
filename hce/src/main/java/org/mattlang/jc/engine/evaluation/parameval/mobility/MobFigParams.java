package org.mattlang.jc.engine.evaluation.parameval.mobility;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

@EvalConfigurable
public class MobFigParams {

    @EvalConfigParam(name = "Mob")
    public MgEgArrayFunction mobility;

    @EvalConfigParam(name = "Tropism")
    public MgEgArrayFunction tropism;

    @EvalConfigParam(name = "KingAttack")
    @EvalValueInterval(min = 0, max = 50)
    public MgEgArrayFunction kingAtt;

    public MobFigParams() {

    }

}
