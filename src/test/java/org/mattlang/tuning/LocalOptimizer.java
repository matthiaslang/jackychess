package org.mattlang.tuning;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;

public class LocalOptimizer implements Optimizer {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizer.class.getSimpleName());

    /**
     * safety delta value to ensure that error is not only better by a minor calculation precision issue.
     */
    public static final double DELTA = 0.00000001;
    private final File outputDir;
    private DataSet dataSet;

    private TuneableEvaluateFunction evaluate;

    public LocalOptimizer(File outputDir) {
        this.outputDir=outputDir;
    }

    @Override
    public List<TuningParameter> optimize(TuneableEvaluateFunction evaluate, DataSet dataSet) {
        this.dataSet = dataSet;
        this.evaluate = evaluate;
        List<TuningParameter> initialGuess = evaluate.getParams();
        return optimize(initialGuess);
    }

    public List<TuningParameter> optimize(List<TuningParameter> initialGuess) {
        int nParams = initialGuess.size();
        double bestE = e(initialGuess);
        LOGGER.info("Error at start: " + bestE);

        List<TuningParameter> bestParValues = initialGuess;
        int round = 0;
        StopWatch stopWatch=new StopWatch();

        boolean improved = true;
        while (improved) {
            improved = false;
            for (int pi = 0; pi < nParams; pi++) {
                round++;
                if (round % 100 == 0 && stopWatch.timeElapsed(5*60000)) {
                    LOGGER.info(stopWatch.getFormattedCurrDuration() + ": round " + round + ", curr Error= " + bestE);
                    LOGGER.info(evaluate.collectParamDescr());
                    evaluate.writeParamDescr(outputDir);
                }

                bestParValues.get(pi).change(1);
                double newE = e(bestParValues);
                if (newE < bestE - DELTA) {
                    bestE = newE;
                    improved = true;
                } else {
                    bestParValues.get(pi).change(-2);
                    newE = e(bestParValues);
                    if (newE < bestE - DELTA) {
                        bestE = newE;
                        improved = true;
                    } else {
                        // reset change:
                        bestParValues.get(pi).change(+1);
                    }
                }
            }
        }
        return bestParValues;
    }

    private double e(List<TuningParameter> params) {

        return dataSet.calcError(evaluate, params);
    }
}
