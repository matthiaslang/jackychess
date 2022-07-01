package org.mattlang.tuning;

import java.util.List;

import org.mattlang.jc.StopWatch;

public class LocalOptimizer implements Optimizer {

    /** safety delta value to ensure that error is not only better by a minor calculation precision issue. */
    public static final double DELTA = 0.00000001;
    private DataSet dataSet;

    private TuneableEvaluateFunction evaluate;

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
        System.out.println("Error at start: " + bestE);

        List<TuningParameter> bestParValues = initialGuess;
        int round = 0;
        StopWatch stopWatch=new StopWatch();

        boolean improved = true;
        while (improved) {
            for (int pi = 0; pi < nParams; pi++) {
                round++;
                if (round % 1000 == 0) {
                    System.out.println(stopWatch.getFormattedCurrDuration()+  ": round " + round + ", best Error= " + bestE);
                }
                improved = false;

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
        evaluate.setParams(params);
        return dataSet.calcError(evaluate);
    }
}
