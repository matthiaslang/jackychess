package org.mattlang.tuning;

import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;

/**
 * Optimizer to optimize the scaling Constant K.
 *
 * Maybe this can be combined with the regular local optimizer code...
 */
public class LocalOptimizerK  {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizerK.class.getSimpleName());

    /**
     * safety delta value to ensure that error is not only better by a minor calculation precision issue.
     */
    public static final double DELTA = 0.00000001;
    private DataSet dataSet;

    private TuneableEvaluateFunction evaluate;


    public double optimize(TuneableEvaluateFunction evaluate, DataSet dataSet) {
        this.dataSet = dataSet;
        this.evaluate = evaluate;
        List<TuningParameter> initialGuess = evaluate.getParams();
        return optimize(initialGuess);
    }

    public double optimize(List<TuningParameter> initialGuess) {
        double bestE = e(initialGuess);
        LOGGER.info("Error at start: " + bestE + " K=" + dataSet.getK());

        List<TuningParameter> bestParValues = initialGuess;
        int round = 0;
        StopWatch stopWatch = new StopWatch();

        double KDELTA = 0.01;

        boolean improved = true;
        while (improved) {
            improved = false;

            round++;
            if (round % 100 == 0 && stopWatch.timeElapsed(60000)) {
                LOGGER.info(stopWatch.getFormattedCurrDuration() + ": round " + round + ", curr Error= " + bestE + " K="
                        + dataSet.getK());
                LOGGER.info(evaluate.collectParamDescr());
            }
            dataSet.setK(dataSet.getK() + KDELTA);
            double newE = e(bestParValues);
            if (newE < bestE - DELTA) {
                bestE = newE;
                improved = true;
            } else {
                dataSet.setK(dataSet.getK() - 2 * KDELTA);
                newE = e(bestParValues);
                if (newE < bestE - DELTA) {
                    bestE = newE;
                    improved = true;
                } else {
                    // reset change:
                    dataSet.setK(dataSet.getK() + KDELTA);
                }
            }

        }

        LOGGER.info("K Optimized: Error: " + bestE + " K=" + dataSet.getK());
        return dataSet.getK();
    }

    private double e(List<TuningParameter> params) {

        return dataSet.calcError(evaluate, params);
    }
}
