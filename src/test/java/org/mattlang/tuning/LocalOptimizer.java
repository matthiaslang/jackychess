package org.mattlang.tuning;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.tuner.OptParameters;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static org.mattlang.jc.board.Color.WHITE;

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
    public List<TuningParameter> optimize(ParamTuneableEvaluateFunction evaluate, DataSet dataSet) {
        this.dataSet = dataSet;
        this.evaluate = evaluate;
        List<TuningParameter> params = evaluate.getParams();

        markdownAppender.append(w -> w.h2("new optimization round"));

        // init optimization:
        if (optParameters.isOptimizeRecalcOnlyDependendFens()) {
            LOGGER.info("Init depending Fen Information... ");
            initDependendFensInformation(params, dataSet);
            LOGGER.info("Init depending Fen Information finished");

            // analyze dependant fens and log:
            analyzeDependantFens(params);
        }

        for (int step : stepGranularity) {
            LOGGER.info("Optimizing with step " + step);
            markdownAppender.append(w -> w.h3("Optimizing with step " + step));
            optimize(params, step);
        }
        return params;
    }

    private void optimize(List<TuningParameter> params, int step) {

        // init bestE value: also important for dependend fen optimization that we have a precalculated error value
        // for each fen:
        double bestE = e(params);
        LOGGER.info("Error at start: " + bestE);


        final double errorAtStart = bestE;
        markdownAppender.append(w -> w.paragraph("Error at start: " + errorAtStart));

        int round = 0;
        StopWatch stopWatch = new StopWatch();

        boolean improved = true;
        while (improved) {
            improved = false;
            if (shuffle) {
                Collections.shuffle(params, random);
            }

            for (TuningParameter param : params) {
                round++;
                if (round % 100 == 0 && stopWatch.timeElapsed(5 * 60000)) {
                    progressInfo(step, bestE, round, stopWatch);
                }

                // if we are within bounds do a step change
                if (param.isChangePossible(step)) {

                    param.change(step);
                    double newE = e(params);
                    if (newE < bestE - delta) {
                        bestE = newE;
                        improved = true;
                    } else if (param.isChangePossible(-2 * step)) {
                        // otherwise try the step in the different direction (if allowed):
                        param.change(-2 * step);
                        newE = e(params);
                        if (newE < bestE - delta) {
                            bestE = newE;
                            improved = true;
                        } else {
                            // reset change:
                            param.change(step);
                        }
                    } else {
                        // change back
                        param.change(-step);
                    }
                }
            }
        }
    }

    private void analyzeDependantFens(List<TuningParameter> params) {
        // if there are params where NO fen depends on, than thats a problem, because it means
        // there is no way to optimize these parameter.
        // so we should at least logout this:

        for (TuningParameter param : params) {
            if (!param.hasDependingFens()) {
                LOGGER.info("No depending Fens in Dataset for " + param.getDescr());
            }
        }
        for (TuningParameter param : params) {
            if (param.hasDependingFens()) {
                LOGGER.info(param.getDescr() + " depending on " + param.getDependingFenCount() + " fens");
            }
        }
    }

    private void initDependendFensInformation(List<TuningParameter> params, DataSet dataSet) {
        for (FenEntry fen : dataSet.getFens()) {
            for (TuningParameter param : params) {
                if (checkFenDependsOnParam(fen, param)) {
                    param.addDependingFen(fen);
                }
            }
        }
    }

    private boolean checkFenDependsOnParam(FenEntry fen, TuningParameter param) {
        int evalOfCurrValue = evaluate.eval(fen.getBoard(), WHITE);

        // check min, max, zero values:
        int minVal = param.getIntervall().getMinIntVal();
        int maxVal = param.getIntervall().getMaxIntVal();

        int evalOfMinValue = checkEval(minVal, fen, param);
        if (evalOfMinValue != evalOfCurrValue) {
            return true;
        }
        int evalOfMaxValue = checkEval(maxVal, fen, param);
        if (evalOfMaxValue != evalOfCurrValue || evalOfMaxValue != evalOfMinValue) {
            return true;
        }
        if (minVal < 0 && maxVal > 0) {
            int evaolOfZeroValue = checkEval(0, fen, param);
            if (evaolOfZeroValue != evalOfCurrValue || evaolOfZeroValue != evalOfMinValue || evaolOfZeroValue != evalOfMaxValue) {
                return true;
            }
        }

        return false;
    }

    /**
     * Evaluate the fen with a different parameter value.
     * Afterwards reset the tuning parameter to its origin value.
     *
     * @param value
     * @param fen
     * @param param
     * @return
     */
    private int checkEval(int value, FenEntry fen, TuningParameter param) {
        // save current value
        int currVal = param.getValue();

        // set the different value:
        param.setValue(value);
        // update evaluation function with parameter value:
        param.saveValue(evaluate.getParameterizedEvaluation());
        // evaluate with that new value:
        int cmpValue = evaluate.eval(fen.getBoard(), WHITE);
        // reset to origin value:
        param.setValue(currVal);
        // update evaluation function with parameter value:
        param.saveValue(evaluate.getParameterizedEvaluation());

        return cmpValue;
    }

    private void progressInfo(int step, double bestE, int round, StopWatch stopWatch) {
        LOGGER.info(stopWatch.getFormattedCurrDuration() + ": round " + round + ", step " + step
                + ", curr Error= " + bestE);
        LOGGER.info(evaluate.collectParamDescr());
        evaluate.writeParamDescr(outputDir);

        markdownAppender.append(w -> {
            w.paragraph(stopWatch.getFormattedCurrDuration() + ": round " + round + ", step " + step
                    + ", curr Error= " + bestE);
        });
    }

    private double e(List<TuningParameter> params) {

        return dataSet.calcError(evaluate, params);
    }
}
