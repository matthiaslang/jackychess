package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.RochadeType;

public class CastlingDef {



    Color side;
    RochadeType rochadeType;

    int[] fieldPos;
    Figure[] fieldPosFigures;
    int[] fieldCheckTst;

    public CastlingDef(Color side, RochadeType rochadeType, int[] fieldPos, Figure[] fieldPosFigures,
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
