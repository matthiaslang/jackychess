package org.mattlang.tuning;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.tuning.data.pgnparser.Ending;

import lombok.Value;

@Value
public class FenEntry {

//    private String fen;

    private BoardRepresentation board;

    private Ending ending;

    private double result;

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
