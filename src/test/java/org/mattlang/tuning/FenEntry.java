package org.mattlang.tuning;

import lombok.Getter;
import lombok.Setter;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.tuning.data.pgnparser.Ending;

@Getter
@Setter
public class FenEntry {

//    private String fen;

    private BoardRepresentation board;

    private Ending ending;

    private double result;

    /**
     * the last calculated error value for that fen. Used in optimization mode where we only recalculate
     * fens which depend on the current parameter.
     */
    private double errorValue;

    public FenEntry(String fen, BoardRepresentation board, Ending ending) {
//        this.fen = fen;
        this.board = board;
        this.ending = ending;

        if (ending == Ending.MATE_WHITE) {
            result = 1;
        } else if (ending == Ending.MATE_BLACK) {
            result = 0;
        } else {
            result = 0.5;
        }
    }

}
