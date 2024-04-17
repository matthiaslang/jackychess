package org.mattlang.tuning.genetic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.tuning.AbstractTuner;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.LocalOptimizerK;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.OptParameters;

public class GeneticTuner extends AbstractTuner {

    private static final Logger LOGGER = Logger.getLogger(GeneticTuner.class.getSimpleName());

    public GeneticTuner(String[] args) {
        super(OptParameters.builder().inputFiles(Arrays.asList(args)).build());
    }

    public GeneticTuner(OptParameters params) {
        super(params);
    }

    public static void main(String[] args) throws IOException {

        GeneticTuner tuner = new GeneticTuner(args);
        tuner.run();

    }

    public static void run(OptParameters params) throws IOException {
        GeneticTuner tuner = new GeneticTuner(params);
        executorService = Executors.newFixedThreadPool(params.getThreadCount());
        tuner.run();
    }

    private void run() throws IOException {

        initRun();

        ParamTuneableEvaluateFunction evaluate =
                new ParamTuneableEvaluateFunction(params);
        copySourceConfigFile(outputDir);

        ParameterSet parameterSet = new ParameterSet(params, evaluate.getParameterizedEvaluation());

        // start evals for initial population:
        List<ParameterSet> startConfigs = new ArrayList<>();
        for (String startEvalConfig : params.getGeneticParams().getStartEvalConfigs()) {
            ParamTuneableEvaluateFunction startEval =
                    new ParamTuneableEvaluateFunction(startEvalConfig, params);
            startConfigs.add(new ParameterSet(params, startEval.getParameterizedEvaluation()));
        }

        // todo doesnt make sense in genetic tuning...
        if (!continuingTuningRun && params.isResetParametersBeforeTuning()) {
            LOGGER.info("Resetting Parameter values");
            for (TuningParameter param : parameterSet.getParams()) {
                param.resetValue();
            }
            parameterSet.writeParamDescr(outputDir);
        }

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadAndPrepareData();

        // write or append the general infos for this run
        markdownAppender.append(w -> {
            params.writeMarkdownInfos(w);
            dataset.writeLogInfos(w);
        });

        if (params.isAdjustK()) {
            LOGGER.info("Minimize Scaling K...");
            LocalOptimizerK optimizerK = new LocalOptimizerK(params);
            double k = optimizerK.optimize(parameterSet, evaluate, dataset);
            LOGGER.info("Scaling finished: K=" + k);
            dataset.setK(k);

            markdownAppender.append(w -> {
                w.paragraph("K adjusted to: " + k);
            });
        } else {
            markdownAppender.append(w -> {
                w.paragraph("K: " + dataset.getK());
            });
        }

        SimpleGeneticAlgorithm optimizer =
                new SimpleGeneticAlgorithm(outputDir, params, evaluate, dataset, markdownAppender);

        LOGGER.info("Initial Parameter values:\n" + parameterSet.collectParamDescr());
        parameterSet.writeParamDescr(outputDir);

        LOGGER.info("Opimizing...");
        ParameterSet optimizedParams = optimizer.runAlgorithm(parameterSet, startConfigs);

        LOGGER.info("Optimized Parameter values:\n" + optimizedParams.collectParamDescr());
        optimizedParams.writeParamDescr(outputDir);

        executorService.shutdown();

    }
    
}
