package org.mattlang.jc.movegenerator;

import java.util.Arrays;
import java.util.Objects;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.CastlingType;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;
import lombok.ToString;

@ToString
public final class CastlingDef {

    private int[] fieldCheckTst;

    private final long rookFromMask;
    private final long kingFromMask;
    private final long emptyMask;

    @Getter
    private final CastlingType castlingType;

    public CastlingDef(CastlingType castlingType,
            int[] fieldCheckTst, long rookFromMask, long kingFromMask, long emptyMask) {
        this.castlingType = castlingType;
        this.fieldCheckTst = fieldCheckTst;

        this.rookFromMask = rookFromMask;

        this.kingFromMask = kingFromMask;
        this.emptyMask = emptyMask;
    }

    /**
     * Checks if this rochade is allowed on the board.
     * Old version
     *
     * @param board
     * @return
     */
    public boolean check(BoardRepresentation board) {
        // check if rochade is still allowed:
        if (board.isCastlingAllowed(castlingType)) {
            // check that the relevant figures and empty fields are as needs to be for castling:
            if (checkPos(board)) {
                // check that king pos and moves are not in check:
                for (int pos : fieldCheckTst) {
                    if (Captures.canFigureCaptured(board, pos, castlingType.getColor())) {
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
        return kingFromMask == bb.getKings(castlingType.getColor())
                && (rookFromMask & bb.getRooks(castlingType.getColor())) != 0
                && (~bb.getPieces() & emptyMask) == emptyMask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CastlingDef that = (CastlingDef) o;
        return rookFromMask == that.rookFromMask && kingFromMask == that.kingFromMask && emptyMask == that.emptyMask
                && Objects.deepEquals(fieldCheckTst, that.fieldCheckTst) && castlingType == that.castlingType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(fieldCheckTst), rookFromMask, kingFromMask, emptyMask, castlingType);
    }
}
