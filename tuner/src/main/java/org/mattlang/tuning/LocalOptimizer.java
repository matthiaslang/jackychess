package org.mattlang.tuning;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.OptParameters;

public class LocalOptimizer implements Optimizer {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizer.class.getSimpleName());

    public final List<Integer> stepGranularity;

    public final double delta;
    private final File outputDir;
    private final MarkdownAppender markdownAppender;
    private final OptParameters optParameters;
    private final boolean shuffle;
    private DataSet dataSet;

    private ParamTuneableEvaluateFunction evaluate;
    private static Random random = new Random(4713112713L);

    public LocalOptimizer(File outputDir, OptParameters optParameters, MarkdownAppender markdownAppender) {
        this.outputDir = outputDir;
        this.optParameters = optParameters;
        this.delta = optParameters.getDelta();
        this.stepGranularity = optParameters.getStepGranularity();
        this.shuffle = optParameters.isShuffleTuningParameter();

        this.markdownAppender = markdownAppender;
    }

    @Override
    public ParameterSet optimize(ParameterSet parameterSet, ParamTuneableEvaluateFunction evaluate, DataSet dataSet) {
        this.dataSet = dataSet;
        this.evaluate = evaluate;

        markdownAppender.append(w -> w.h2("new optimization round"));

        for (int step : stepGranularity) {
            LOGGER.info("Optimizing with step " + step);
            markdownAppender.append(w -> w.h3("Optimizing with step " + step));
            optimize(parameterSet, step);
        }
        return parameterSet;
    }

    private void optimize(ParameterSet parameterSet, int step) {

        ProgressParams progress = new ProgressParams();

        // init bestE value: also important for dependend fen optimization that we have a precalculated error value
        // for each fen:
        progress.bestE = e(parameterSet);

        LOGGER.info("Error at start: " + progress.bestE);

        final double errorAtStart = progress.bestE;
        markdownAppender.append(w -> w.paragraph("Error at start: " + errorAtStart));

        ProgressInfo progressInfo = new ProgressInfo(optParameters, outputDir, markdownAppender);

        while (progress.improved && progressInfo.hasEnoughProgress(step)) {
            progress.improved = false;
            if (shuffle) {
                Collections.shuffle(parameterSet.getParams(), random);
            }

            progress.paramIterationRound++;

            for (TuningParameter param : parameterSet.getParams()) {
                progress.round++;
                if (progress.round % 100 == 0 && progressInfo.isEnoughTimeElapsed()) {
                    progressInfo.progressInfo(parameterSet, step, progress);
                }

                // if we are within bounds do a step change
                if (param.isChangePossible(step)) {

                    change(param, step);
                    double newE = e(parameterSet);
                    if (newE < progress.bestE - delta) {
                        progress.bestE = newE;
                        progress.improved = true;
                        progress.numParamAdjusted++;
                        param.incAdjCounter();
                    } else if (param.isChangePossible(-2 * step)) {
                        // otherwise try the step in the different direction (if allowed):
                        change(param, -2 * step);
                        newE = e(parameterSet);
                        if (newE < progress.bestE - delta) {
                            progress.bestE = newE;
                            progress.improved = true;
                            progress.numParamAdjusted++;
                            param.incAdjCounter();
                        } else {
                            // reset change:
                            change(param, step);
                        }
                    } else {
                        // change back
                        change(param, -step);
                    }
                }
            }
            // write last info to output
            progressInfo.progressInfo(parameterSet, step, progress);
        }
    }

    private void change(TuningParameter param, int change) {
        param.change(change);
        if (optParameters.isOptimizeMode()) {
            // reset the evalcomponent cached values for this parameter:
            String paramName = param.getParameterName();
            String evalCompName = paramName.substring(0, paramName.indexOf('.'));
            dataSet.resetCachedComponentValues(evalCompName);
        }
    }

    private double e(ParameterSet params) {
        return dataSet.calcError(evaluate, params);
    }
}
