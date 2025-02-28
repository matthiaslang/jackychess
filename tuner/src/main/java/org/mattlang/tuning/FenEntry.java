package org.mattlang.tuning;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.TuningCache;
import org.mattlang.tuning.data.pgnparser.Ending;

import lombok.Data;

@Data
public class FenEntry {

    public static final int NO_EVAL = 1000000;

    private BoardRepresentation board;

    private Ending ending;

    /**
     * the last calculated error value for that fen. Used in optimization mode where we only recalculate
     * fens which depend on the current parameter.
     */
    private int lastEval = NO_EVAL;

    private String comment;

    private TuningCache tuningCache = new TuningCache();

    public FenEntry(String fen, BoardRepresentation board, Ending ending, String comment) {
        //        this.fen = fen;
        this.board = board;
        this.ending = ending;
        this.comment = comment;
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


}
