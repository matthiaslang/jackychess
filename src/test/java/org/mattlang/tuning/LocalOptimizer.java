package org.mattlang.tuning;

import java.util.List;

public class LocalOptimizer implements Optimizer {

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
        List<TuningParameter> bestParValues = initialGuess;
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int pi = 0; pi < nParams; pi++) {
                bestParValues.get(pi).change(1);
                double newE = e(bestParValues);
                if (newE < bestE) {
                    bestE = newE;
                    improved = true;
                } else {
                    bestParValues.get(pi).change(-2);
                    newE = e(bestParValues);
                    if (newE < bestE) {
                        bestE = newE;
                        improved = true;
                    } else {
                        // reset change:
                        bestParValues.get(pi).change(+2);
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
