package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.RochadeType;

public enum CastlingDef {

    ROCHADE_L_WHITE(
            WHITE,
            RochadeType.LONG,
            new int[] { 0, 1, 2, 3, 4 },
            new Figure[] { W_Rook, EMPTY, EMPTY, EMPTY, W_King },
            new int[] { 2, 3, 4 }),

    ROCHADE_S_WHITE(
            WHITE,
            RochadeType.SHORT,
            new int[] { 4, 5, 6, 7 },
            new Figure[] { W_King, EMPTY, EMPTY, W_Rook },
            new int[] { 4, 5, 6 }),

    ROCHADE_S_BLACK(
            BLACK,
            RochadeType.SHORT,
            new int[] { 60, 61, 62, 63 },
            new Figure[] { B_King, EMPTY, EMPTY, B_Rook },
            new int[] { 60, 61, 62 }),

    ROCHADE_L_BLACK(
            BLACK,
            RochadeType.LONG,
            new int[] { 56, 57, 58, 59, 60 },
            new Figure[] { B_Rook, EMPTY, EMPTY, EMPTY, B_King },
            new int[] { 58, 59, 60 });

    Color side;
    RochadeType rochadeType;

    int[] fieldPos;
    Figure[] fieldPosFigures;
    int[] fieldCheckTst;

    CastlingDef(Color side, RochadeType rochadeType, int[] fieldPos, Figure[] fieldPosFigures,
            int[] fieldCheckTst) {
        this.side = side;
        this.rochadeType = rochadeType;
        this.fieldPos = fieldPos;
        this.fieldPosFigures = fieldPosFigures;
        this.fieldCheckTst = fieldCheckTst;
    }

    /**
     * Checks if this rochade is allowed on the board.
     * Old version
     * @param board
     * @return
     */
    @Deprecated
    public boolean check(BoardRepresentation board) {
        // check if rochade is still allowed:
        if (board.isCastlingAllowed(side, rochadeType)) {
            // check that the relevant figures and empty fields are as needs to be for castling:
            if (checkPos(board)) {
                // check that king pos and moves are not in check:
                for (int pos : fieldCheckTst) {
                    if (Captures.canFigureCaptured(board, pos, side)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkPos(BoardRepresentation board) {
        for (int i = 0; i < fieldPos.length; i++) {
            Figure figure = board.getPos(fieldPos[i]);
            if (fieldPosFigures[i] != figure) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this rochade is allowed using bitboard stuff.
     *
     * @param board
     * @return
     */

    // todo implement via bitboard comparisons
    public boolean checkRochade(BoardRepresentation board) {
        // check if rochade is still allowed:
        if (board.isCastlingAllowed(side, rochadeType)) {
            // check that the relevant figures and empty fields are as needs to be for castling:
            if (checkPos(board)) {
                // check that king pos and moves are not in check:
                for (int pos : fieldCheckTst) {
                    if (Captures.canFigureCaptured(board, pos, side)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
