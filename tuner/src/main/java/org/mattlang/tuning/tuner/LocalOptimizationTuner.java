package org.mattlang.tuning.tuner;

import static org.mattlang.jc.command.Main.consoleOut;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;
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
                new ParamTuneableEvaluateFunction(params, continuingTuningRun);

        if (!continuingTuningRun) {
            copySourceConfigFile(outputDir, evaluate.getParameterizedEvaluation());
        }

        ParameterSet parameterSet = new ParameterSet(params, evaluate.getParameterizedEvaluation());

        if (!continuingTuningRun && params.isResetParametersBeforeTuning()) {
            consoleOut("Resetting Parameter values");
            for (TuningParameter param : parameterSet.getParams()) {
                param.resetValue();
            }
            consoleOut("Resetting Parameter values");
            LOGGER.info("Resetted Parameter values:\n" + parameterSet.collectParamDescr());
            parameterSet.writeParamDescr(outputDir);
        }

        consoleOut("Load & Prepare Data...");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DataSet dataset = loadAndPrepareData();
        stopWatch.stop();

        consoleOut("Prepare Data took " + stopWatch.getFormattedDuration());

        // write or append the general infos for this run
        markdownAppender.append(w -> {
            params.writeMarkdownInfos(w, parameterSet);
            if (!continuingTuningRun) {
                dataset.writeLogInfos(w);
            }
        });

        if (params.isAdjustK() && params.getK() != null) {
            throw new IllegalArgumentException("Parameter adjustK and K both set does not make sense!");
        }

        if (params.isAdjustK()) {
            consoleOut("Minimize Scaling K...");
            LocalOptimizerK optimizerK = new LocalOptimizerK(params);
            double k = optimizerK.optimize(parameterSet, evaluate, dataset);
            consoleOut("Scaling finished: K=" + k);
            dataset.setScalingK(k);

            markdownAppender.append(w -> w.paragraph("K adjusted to: " + k));
        } else {
            if (params.getK() != null) {
                dataset.setScalingK(params.getK());
                markdownAppender.append(w -> {
                    w.paragraph("setting K from Input Tuner Parameter to: " + dataset.getScalingK());
                });
            }
            markdownAppender.append(w -> w.paragraph("K: " + dataset.getScalingK()));
        }

        LocalOptimizer optimizer = new LocalOptimizer(outputDir, params, markdownAppender);

        markdownAppender.append(w-> {
            w.h2("Initial Parameter values");
            w.codeBlock(parameterSet.collectParamDescr());
        });
        parameterSet.writeParamDescr(outputDir);

        consoleOut("Optimizing...");
        ParameterSet optimizedParams = optimizer.optimize(parameterSet, evaluate, dataset);

        parameterSet.writeParamDescr(outputDir);

        markdownAppender.append(w-> {
            w.h2("Parameter after tuning");
            w.codeBlock(parameterSet.collectParamDescr());
        });


        executorService.shutdown();

    }

}
