package org.mattlang.tuning;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;

import lombok.Value;

/**
 * Contains all FEN Positions used as datas set for tuning.
 */
@Value
public class DataSet {

    /**
     * scaling Constant.
     */
    private static final double K = 1.13;
    private List<FenEntry> fens = new ArrayList<>();

    public double calcError(EvaluateFunction evaluate) {
        // build sum:
        double sum = 0;
        for (FenEntry fen : fens) {
            sum += square(fen.getResult() - sigmoid(evaluate.eval(fen.getBoard(), Color.WHITE)));
        }
        return sum / fens.size();

    }

    private double sigmoid(int eval) {
        double deval = eval;
        return 1 / (1 + Math.pow(10, -K * deval / 400));
    }

    private double square(double val) {
        return val * val;
    }

    public void addFen(FenEntry entry) {
        fens.add(entry);
    }
}
