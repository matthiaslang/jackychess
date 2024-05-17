package org.mattlang.tuning.tuner;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.tuning.*;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;

public class LocalOptimizationTuner extends AbstractTuner {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizationTuner.class.getSimpleName());

    public LocalOptimizationTuner(String[] args) {
        super(OptParameters.builder().inputFiles(Arrays.asList(args)).build());
    }

    public LocalOptimizationTuner(OptParameters params) {
        super(params);
    }

    public static void main(String[] args) throws IOException {

        LocalOptimizationTuner tuner = new LocalOptimizationTuner(args);
        tuner.run();

    }

    public static void run(OptParameters params) throws IOException {
        LocalOptimizationTuner tuner = new LocalOptimizationTuner(params);
        executorService = Executors.newFixedThreadPool(params.getThreadCount());
        tuner.run();
    }

    private void run() throws IOException {

        initRun();

        ParamTuneableEvaluateFunction evaluate =
                new ParamTuneableEvaluateFunction(params);

        copySourceConfigFile(outputDir);

        ParameterSet parameterSet = new ParameterSet(params, evaluate.getParameterizedEvaluation());

        if (!continuingTuningRun && params.isResetParametersBeforeTuning()) {
            LOGGER.info("Resetting Parameter values");
            for (TuningParameter param : parameterSet.getParams()) {
                param.resetValue();
            }
            LOGGER.info("Resetted Parameter values:\n" + parameterSet.collectParamDescr());
            parameterSet.writeParamDescr(outputDir);
        }

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadAndPrepareData();

        // write or append the general infos for this run
        markdownAppender.append(w -> {
            params.writeMarkdownInfos(w);
            dataset.writeLogInfos(w);
        });

        if (params.isAdjustK() && params.getK() != null) {
            throw new IllegalArgumentException("Parameter adjustK and K both set does not make sense!");
        }

        if (params.isAdjustK()) {
            LOGGER.info("Minimize Scaling K...");
            LocalOptimizerK optimizerK = new LocalOptimizerK(params);
            double k = optimizerK.optimize(parameterSet, evaluate, dataset);
            LOGGER.info("Scaling finished: K=" + k);
            dataset.setK(k);

            markdownAppender.append(w -> w.paragraph("K adjusted to: " + k));
        } else {
            if (params.getK() != null) {
                dataset.setK(params.getK());
                markdownAppender.append(w -> {
                    w.paragraph("setting K from Input Tuner Parameter to: " + dataset.getK());
                });
            }
            markdownAppender.append(w -> w.paragraph("K: " + dataset.getK()));
        }

        LocalOptimizer optimizer = new LocalOptimizer(outputDir, params, markdownAppender);

        LOGGER.info("Initial Parameter values:\n" + parameterSet.collectParamDescr());
        parameterSet.writeParamDescr(outputDir);

        LOGGER.info("Opimizing...");
        ParameterSet optimizedParams = optimizer.optimize(parameterSet, evaluate, dataset);

        LOGGER.info("Optimized Parameter values:\n" + parameterSet.collectParamDescr());
        parameterSet.writeParamDescr(outputDir);

        executorService.shutdown();

    }

}
