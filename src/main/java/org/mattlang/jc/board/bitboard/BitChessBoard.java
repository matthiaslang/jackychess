package org.mattlang.jc.board.bitboard;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import java.util.Arrays;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;

/**
 * The chess board as bit board implementation.
 *
 * Encapsulates a bit board representation via 64 field access (as in existing 64 byte representation).
 * Of course this needs then step-by-step been refactored to make real use of all the bitboard benefits.
 */
public class BitChessBoard {

    private long[] colorBB = new long[2];
    private long[] pieceBB = new long[6];

    private static final int nWhite = 0;     // any white piece
    private static final int nBlack = 1;     // any black piece

    public BitChessBoard() {

    }

    public BitChessBoard(long[] colorBB, long[] pieceBB) {
        this.colorBB = colorBB;
        this.pieceBB = pieceBB;
    }

    long getPieceSet(int pt, int color) {
        return pieceBB[pt] & colorBB[color];
    }

    long getWhitePawns() {
        return pieceBB[FT_PAWN] & colorBB[nWhite];
    }

    long getPawns(int color) {
        return getPieceSet(FT_PAWN, color);
    }

    public void set(int i, byte figureCode) {
        long posMask = 1L << i;

        byte figType = (byte) (figureCode & MASK_OUT_COLOR);
        int colorIdx = ((figureCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;

        if (figureCode == FigureConstants.FT_EMPTY || figureCode == 0) {
            colorBB[colorIdx] &= ~posMask;
            pieceBB[figType] &= ~posMask;
        } else {
            colorBB[colorIdx] |= posMask;
            pieceBB[figType] |= posMask;
        }

    }

    public byte get(int i) {
        long posMask = 1L << i;
        byte colorOffset;
        if ((colorBB[nWhite] & posMask) != 0) {
            colorOffset = Color.WHITE.code;
        } else if ((colorBB[nBlack] & posMask) != 0) {
            colorOffset = BLACK.code;
        } else {
            return FigureConstants.FT_EMPTY;
        }

        for (int figType = 0; figType < 6; figType++) {
            if ((pieceBB[figType] & posMask) != 0) {
                return (byte) (colorOffset + figType);
            }
        }
        throw new IllegalStateException("no valid bit mask code existing!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BitChessBoard that = (BitChessBoard) o;
        return Arrays.equals(colorBB, that.colorBB) && Arrays.equals(pieceBB, that.pieceBB);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(colorBB);
        result = 31 * result + Arrays.hashCode(pieceBB);
        return result;
    }

    public BitChessBoard copy() {
        return new BitChessBoard(colorBB.clone(), pieceBB.clone());
    }
}
