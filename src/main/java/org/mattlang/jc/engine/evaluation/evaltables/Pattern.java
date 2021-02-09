package org.mattlang.jc.engine.evaluation.evaltables;

import org.mattlang.jc.board.PieceList;

/**
 * A board pattern with evaluations per field.
 */
public class Pattern {

    private final byte[] boardPattern;

    private final byte[] flippedPattern;

    /* The FLIP array is used to calculate the piece/square values for the oppiste pieces.
     The piece/square value of a LIGHT pawn is PAWN_PCSQ[FLIP[sq]] and the value of a
    DARK pawn is PAWN_PCSQ[sq]

     */

    // todo optimization could be to cache the flipped pattern to save the flipping for each calc
    private byte[] FLIP = new byte[] {
            56, 57, 58, 59, 60, 61, 62, 63,
            48, 49, 50, 51, 52, 53, 54, 55,
            40, 41, 42, 43, 44, 45, 46, 47,
            32, 33, 34, 35, 36, 37, 38, 39,
            24, 25, 26, 27, 28, 29, 30, 31,
            16, 17, 18, 19, 20, 21, 22, 23,
            8, 9, 10, 11, 12, 13, 14, 15,
            0, 1, 2, 3, 4, 5, 6, 7
    };

    public Pattern(byte[] boardPattern) {
        this.boardPattern = boardPattern;
        this.flippedPattern = new byte[64];
        for(int i=0; i<64; i++) {
            flippedPattern[i] = boardPattern[FLIP[i]];
        }
    }

    public final int calcScore(PieceList.Array whiteFigures, PieceList.Array blackFigures, int who2mov) {
        int w = 0;
        int b = 0;
        for (int figure : whiteFigures.getArr()) {
            w += flippedPattern[figure];
        }
        for (int figure : blackFigures.getArr()) {
            b += boardPattern[figure];
        }
        return (w - b) * who2mov;
    }

    public final int calcScore(int whiteFigure, int blackFigure, int who2mov) {
        int w = 0;
        int b = 0;
        w += flippedPattern[whiteFigure];
        b += boardPattern[blackFigure];

        return (w - b) * who2mov;
    }
}
