package org.mattlang.tuning;

import static java.lang.Math.pow;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mattlang.jc.engine.EvaluateFunction;

import lombok.Data;

/**
 * Contains all FEN Positions used as datas set for tuning.
 */
@Data
public class DataSet {

    /**
     * scaling Constant.
     */
    private static final double K = 1.13;
    private List<FenEntry> fens = new ArrayList<>();
    /**
     * hold to create copies for the trhead local.
     */
    private TuneableEvaluateFunction evaluate;
    private List<TuningParameter> params;

    private boolean multithreaded = false;

    public double calcError(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        if (multithreaded) {
            return calcMultiThreaded(evaluate, params);
        } else {
            return calcSingleThreaded(evaluate, params);
        }
    }

    private double calcSingleThreaded(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.setParams(params);
        // build sum:
        double sum = 0;
        for (FenEntry fen : fens) {
            sum += pow(fen.getResult() - sigmoid(evaluate.eval(fen.getBoard(), WHITE)), 2);
        }
        return sum / fens.size();

    }

    public double calcMultiThreaded(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.setParams(params);
        this.evaluate = evaluate;
        this.params = params;

        // build sum:

        Optional<Double> sum2 = fens.stream()
                .parallel()
                .map(fen -> pow(fen.getResult() - sigmoid(getThreadLocalEval().eval(fen.getBoard(), WHITE)), 2))
                .reduce(Double::sum);

        return sum2.get() / fens.size();

    }

    private ThreadLocal<EvaluateFunction> threadLocalEvaluateFunction = new ThreadLocal<EvaluateFunction>() {

        @Override
        protected EvaluateFunction initialValue() {
            TuneableEvaluateFunction copy = evaluate.copy();
            copy.setParams(params);
            return copy;
        }
    };

    private EvaluateFunction getThreadLocalEval() {
        return threadLocalEvaluateFunction.get();
    }

    private double sigmoid(int eval) {
        double deval = eval;
        return 1 / (1 + pow(10, -K * deval / 400));
    }

    public void addFen(FenEntry entry) {
        fens.add(entry);
    }

    public void add(DataSet other) {
        fens.addAll(other.getFens());
    }
}
