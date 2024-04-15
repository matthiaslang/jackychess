package org.mattlang.tuning;

import java.util.BitSet;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.tuning.data.pgnparser.Ending;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FenEntry {

    public static final int NO_EVAL = 1000000;

    private BoardRepresentation board;

    private Ending ending;

    /**
     * the last calculated error value for that fen. Used in optimization mode where we only recalculate
     * fens which depend on the current parameter.
     */
    private int lastEval = NO_EVAL;

    private BitSet dependingParams = new BitSet();

    public FenEntry(String fen, BoardRepresentation board, Ending ending) {
        //        this.fen = fen;
        this.board = board;
        this.ending = ending;
    }

    public double getResult() {
        if (ending == Ending.MATE_WHITE) {
            return 1;
        } else if (ending == Ending.MATE_BLACK) {
            return 0;
        } else {
            return 0.5;
        }
    }

    public void addDependingParameter(TuningParameter param) {
        dependingParams.set(param.getParamNo());
    }

    public void resetIfDepending(int paramNo) {
        if (dependingParams.get(paramNo)) {
            lastEval = NO_EVAL;
        }
    }
}
