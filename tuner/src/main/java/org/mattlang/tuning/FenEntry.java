package org.mattlang.tuning;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.TuningCache;
import org.mattlang.tuning.data.pgnparser.Ending;

import lombok.Data;

@Data
public class FenEntry {

    private BoardRepresentation board;

    private Ending ending;

    private String comment;

    private TuningCache tuningCache;

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

    public TuningCache getTuningCache() {
        if (tuningCache == null) {
            tuningCache = new TuningCache();
        }
        return tuningCache;
    }
}
