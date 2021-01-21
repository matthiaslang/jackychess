package org.mattlang.jc.engine.evaluation.evaltables;

import org.mattlang.jc.board.PieceList;

/**
 * A board pattern with evaluations per field.
 */
public class Pattern {

    private byte[] boardPattern;

    public Pattern(byte[] boardPattern) {
        this.boardPattern = boardPattern;
    }

    public final int calcScore(PieceList.Array whiteFigures, PieceList.Array blackFigures, int who2mov) {
        int w = 0;
        int b = 0;
        for (int figure : whiteFigures.getArr()) {
            w += boardPattern[63 - figure];
        }
        for (int figure : blackFigures.getArr()) {
            b += boardPattern[figure];
        }
        return (w - b) * who2mov;
    }

}
