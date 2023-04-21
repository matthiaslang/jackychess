package org.mattlang.jc.movegenerator;

import java.util.Arrays;
import java.util.Objects;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.RochadeType;
import org.mattlang.jc.board.bitboard.BitChessBoard;

public final class CastlingDef {

    private Color side;
    private RochadeType rochadeType;

    private int[] fieldPos;
    private Figure[] fieldPosFigures;
    private int[] fieldCheckTst;

    private final long rookFromMask;
    private final long kingFromMask;
    private final long emptyMask;

    public CastlingDef(Color side, RochadeType rochadeType, int[] fieldPos, Figure[] fieldPosFigures,
            int[] fieldCheckTst, long rookFromMask, long kingFromMask, long emptyMask) {
        this.side = side;
        this.rochadeType = rochadeType;
        this.fieldPos = fieldPos;
        this.fieldPosFigures = fieldPosFigures;
        this.fieldCheckTst = fieldCheckTst;

        this.rookFromMask = rookFromMask;

        this.kingFromMask = kingFromMask;
        this.emptyMask = emptyMask;
    }

    /**
     * Checks if this rochade is allowed on the board.
     * Old version
     * @param board
     * @return
     */
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
        BitChessBoard bb = board.getBoard();
        return kingFromMask == bb.getKings(side)
                && (rookFromMask & bb.getRooks(side)) != 0
                && (~bb.getPieces() & emptyMask) == emptyMask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CastlingDef that = (CastlingDef) o;
        return side == that.side && rochadeType == that.rochadeType && Arrays.equals(fieldPos, that.fieldPos)
                && Arrays.equals(fieldPosFigures, that.fieldPosFigures) && Arrays.equals(fieldCheckTst,
                that.fieldCheckTst);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(side, rochadeType);
        result = 31 * result + Arrays.hashCode(fieldPos);
        result = 31 * result + Arrays.hashCode(fieldPosFigures);
        result = 31 * result + Arrays.hashCode(fieldCheckTst);
        return result;
    }
}
