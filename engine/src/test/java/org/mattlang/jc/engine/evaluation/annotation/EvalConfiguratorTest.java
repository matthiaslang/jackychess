package org.mattlang.jc.engine.evaluation.annotation;

import static org.mattlang.jc.Main.initLogging;

import org.junit.Test;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

public class EvalConfiguratorTest {

    @Test
    public void test() {
        System.setProperty("jacky.logging.activate", "true");
        initLogging();

        EvalConfig config = new EvalConfig();

        ParameterizedEvaluation evaluation = new ParameterizedEvaluation();
        EvalConfigurator evalConfigurator = new EvalConfigurator(config);
        evalConfigurator.configure(evaluation);


    }
}