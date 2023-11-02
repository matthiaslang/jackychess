package org.mattlang.tuning;

import static org.mattlang.jc.board.Color.WHITE;

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

        // init optimization:
        if (optParameters.isOptimizeRecalcOnlyDependendFens()) {
            LOGGER.info("Init depending Fen Information... ");
            initDependendFensInformation(parameterSet, dataSet);
            LOGGER.info("Init depending Fen Information finished");

            // analyze dependant fens and log:
            analyzeDependantFens(parameterSet);
        }

        for (int step : stepGranularity) {
            LOGGER.info("Optimizing with step " + step);
            markdownAppender.append(w -> w.h3("Optimizing with step " + step));
            optimize(parameterSet, step);
        }
        return parameterSet;
    }

    private void optimize(ParameterSet parameterSet, int step) {

        // init bestE value: also important for dependend fen optimization that we have a precalculated error value
        // for each fen:
        double bestE = e(parameterSet);
        LOGGER.info("Error at start: " + bestE);

        final double errorAtStart = bestE;
        markdownAppender.append(w -> w.paragraph("Error at start: " + errorAtStart));

        int round = 0;
        int numParamAdjusted = 0;

        ProgressInfo progressInfo = new ProgressInfo(optParameters, outputDir, markdownAppender);

        boolean improved = true;
        while (improved) {
            improved = false;
            if (shuffle) {
                Collections.shuffle(parameterSet.getParams(), random);
            }

            for (TuningParameter param : parameterSet.getParams()) {
                round++;
                if (round % 100 == 0 && progressInfo.isEnoughTimeElapsed()) {
                    progressInfo.progressInfo(parameterSet, step, bestE, round, numParamAdjusted);
                }

                // if we are within bounds do a step change
                if (param.isChangePossible(step)) {

                    change(param, step);
                    double newE = e(parameterSet);
                    if (newE < bestE - delta) {
                        bestE = newE;
                        improved = true;
                        numParamAdjusted++;
                    } else if (param.isChangePossible(-2 * step)) {
                        // otherwise try the step in the different direction (if allowed):
                        change(param, -2 * step);
                        newE = e(parameterSet);
                        if (newE < bestE - delta) {
                            bestE = newE;
                            improved = true;
                            numParamAdjusted++;
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
        }
    }

    private void change(TuningParameter param, int change) {
        param.change(change);
        if (optParameters.isOptimizeRecalcOnlyDependendFens()) {
            dataSet.resetDependingFens(param);
        }
    }

    private void analyzeDependantFens(ParameterSet parameterSet) {
        // if there are params where NO fen depends on, than thats a problem, because it means
        // there is no way to optimize these parameter.
        // so we should at least logout this:

        for (TuningParameter param : parameterSet.getParams()) {
            if (!param.hasDependingFens()) {
                LOGGER.info("No depending Fens in Dataset for " + param.getDescr());
            }
        }
        for (TuningParameter param : parameterSet.getParams()) {
            if (param.hasDependingFens()) {
                LOGGER.info(param.getDescr() + " depending on " + param.getDependingFenCount() + " fens");
            }
        }
    }

    private void initDependendFensInformation(ParameterSet parameterSet, DataSet dataSet) {
        int i = 0;
        int percent = 0;
        int numFens = dataSet.getFens().size();
        for (FenEntry fen : dataSet.getFens()) {
            i++;
            int currPercent = i * 100 / numFens;
            if (currPercent != percent) {
                percent = currPercent;
                LOGGER.info("processed " + percent + "% of fen dependencies");
            }
            for (TuningParameter param : parameterSet.getParams()) {
                if (checkFenDependsOnParam(fen, param)) {
                    param.addDependingFen(fen);
                    fen.addDependingParameter(param);
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
            if (evaolOfZeroValue != evalOfCurrValue || evaolOfZeroValue != evalOfMinValue
                    || evaolOfZeroValue != evalOfMaxValue) {
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

    private double e(ParameterSet params) {
        return dataSet.calcError(evaluate, params);
    }
}
