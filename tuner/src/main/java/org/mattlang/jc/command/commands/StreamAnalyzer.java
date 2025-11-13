package org.mattlang.jc.command.commands;

import static java.lang.Math.pow;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.command.Main.consoleOut;
import static org.mattlang.tuning.DataSet.DEFAULT_K;

import java.util.HashMap;
import java.util.Map;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.FenEntry;

public class StreamAnalyzer {

    private ParameterizedEvaluation evaluate = ParameterizedEvaluation.createForTuning(new EvalConfig(), false);

    /**
     * sumf of error.
     */
    private double sum = 0;

    /**
     * the fens that look like the eval and the outcome look reasonable.
     */
    private int countOfReasonableOnes = 0;

    private int count = 0;

    private double currError = 0;


    private int mateWhite=0;
    private int mateBlack=0;
    private int draws=0;
    Map<Long, Long> countsByPhase=new HashMap<>();

    /**
     * scaling Constant.
     */
    private double scalingK = DEFAULT_K;

    public void writeAnalyzeOutput() {
        consoleOut("MATE White " +  mateWhite);
        consoleOut("MATE Black " + mateBlack);
        consoleOut("Draws " + draws);

        consoleOut("Error of file " + currError);
        consoleOut("Reasonable fens " + countOfReasonableOnes * 100 / count + "%");
    }

    public void analyze(FenEntry entry) {
        count++;
        int eval = evaluate.eval(entry.getBoard(), WHITE.ordinal());
        double errorValue = pow(entry.getResult() - sigmoid(eval), 2);
        sum += errorValue;

        currError = sum / count;

        // reasonable counter:
        switch (entry.getEnding()) {
        case MATE_WHITE:
            mateWhite++;
            if (eval > 0) {
                countOfReasonableOnes++;
            }
            break;
        case MATE_BLACK:
            mateBlack++;
            if (eval < 0) {
                countOfReasonableOnes++;
            }
            break;
        case DRAW:
            draws++;
            if (Math.abs(eval) < 50) {
                countOfReasonableOnes++;
            }
            break;

        }
    }

    private double sigmoid(int eval) {
        double deval = eval;
        return 1 / (1 + pow(10, -scalingK * deval / 400));
    }
}
