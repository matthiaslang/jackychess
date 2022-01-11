package org.mattlang.jc.board.bitboard;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;

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

    public static final int nWhite = Color.WHITE.ordinal();     // any white piece
    public static final int nBlack = Color.BLACK.ordinal();     // any black piece

    public BitChessBoard() {
    }

    public BitChessBoard(long[] colorBB, long[] pieceBB) {
        this.colorBB = colorBB;
        this.pieceBB = pieceBB;
    }

    public long getPieceSet(int pt, int color) {
        return pieceBB[pt] & colorBB[color];
    }

    public long getPieceSet(int pt, Color color) {
        return pieceBB[pt] & colorBB[color == WHITE ? nWhite : nBlack];
    }

    public int getKnightsCount() {
        return Long.bitCount(pieceBB[FT_KNIGHT]);
    }

    public int getBishopsCount() {
        return Long.bitCount(pieceBB[FT_BISHOP]);
    }

    public int getRooksCount() {
        return Long.bitCount(pieceBB[FT_ROOK]);
    }

    public int getQueensCount() {
        return Long.bitCount(pieceBB[FT_QUEEN]);
    }

    public int getKnightsCount(int color) {
        return Long.bitCount(getPieceSet(FT_KNIGHT, color));
    }
    public int getPawnsCount(int color) {
        return Long.bitCount(getPieceSet(FT_PAWN, color));
    }

    public int getBishopsCount(int color) {
        return Long.bitCount(getPieceSet(FT_BISHOP, color));
    }

    public int getRooksCount(int color) {
        return Long.bitCount(getPieceSet(FT_ROOK, color));
    }

    public int getQueensCount(int color) {
        return Long.bitCount(getPieceSet(FT_QUEEN, color));
    }

    public long getPawns(int color) {
        return getPieceSet(FT_PAWN, color);
    }

    public long getKnights(int color) {
        return getPieceSet(FT_KNIGHT, color);
    }

    public long getBishops(int color) {
        return getPieceSet(FT_BISHOP, color);
    }

    public long getRooks(int color) {
        return getPieceSet(FT_ROOK, color);
    }

    public long getQueens(int color) {
        return getPieceSet(FT_QUEEN, color);
    }

    public long getKings(int color) {
        return getPieceSet(FT_KING, color);
    }

    public long getColorMask(Color color) {
        return color == WHITE ? colorBB[nWhite] : colorBB[nBlack];
    }

    public void set(int i, byte figureCode) {
        long posMask = 1L << i;

        long invPosMask = ~posMask;
        if (figureCode == FigureConstants.FT_EMPTY || figureCode == 0) {
            colorBB[nWhite] &= invPosMask;
            colorBB[nBlack] &= invPosMask;
            for (int figType = 0; figType < 6; figType++) {
                pieceBB[figType] &= invPosMask;
            }
        } else {
            int colorIdx = ((figureCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;
            int otherColor = colorIdx == nWhite ? nBlack : nWhite;
            byte figType = (byte) (figureCode & MASK_OUT_COLOR);
            colorBB[colorIdx] |= posMask;
            colorBB[otherColor] &= invPosMask;

            for (int ft = 0; ft < 6; ft++) {
                if (ft == figType) {
                    pieceBB[ft] |= posMask;
                } else {
                    pieceBB[ft] &= invPosMask;
                }
            }
        }

    }

    public void setByMove(int i, byte figureCode) {
        long posMask = 1L << i;

        long invPosMask = ~posMask;

        int colorIdx = ((figureCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;
        int otherColor = colorIdx == nWhite ? nBlack : nWhite;
        byte figType = (byte) (figureCode & MASK_OUT_COLOR);
        colorBB[colorIdx] |= posMask;
        colorBB[otherColor] &= invPosMask;

        pieceBB[figType] |= posMask;

    }

    public void cleanPos(int i) {
        long posMask = 1L << i;

        long invPosMask = ~posMask;

        colorBB[nWhite] &= invPosMask;
        colorBB[nBlack] &= invPosMask;
        for (int figType = 0; figType < 6; figType++) {
            pieceBB[figType] &= invPosMask;
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

    public static String toStrBoard(long bb) {
        return BB.toStrBoard(bb);
    }

    public static String toStr(long bb) {
        return BB.toStr(bb);
    }

    public void move(int from, int to, byte figureCode, int color) {

        byte figType = (byte) (figureCode & MASK_OUT_COLOR);
        long fromBB = 1L << from;
        long toBB = 1L << to;
        long fromToBB = fromBB ^ toBB; // |+
        pieceBB[figType] ^= fromToBB;   // update piece bitboard
        colorBB[color] ^= fromToBB;   // update white or black color bitboard
        //occupiedBB            ^=  fromToBB;   // update occupied ...
        //emptyBB               ^=  fromToBB;   // ... and empty bitboard

    }

    public void move(int from, int to, byte figureCode, int color, byte capturedPiece) {

        byte figType = (byte) (figureCode & MASK_OUT_COLOR);

        long fromBB = 1L << from;
        long toBB = 1L << to;
        long fromToBB = fromBB ^ toBB; // |+
        pieceBB[figType] ^= fromToBB;   // update piece bitboard
        colorBB[color] ^= fromToBB;   // update white or black color bitboard

        if (capturedPiece != 0) {
            byte cPiece = (byte) (capturedPiece & MASK_OUT_COLOR);
            pieceBB[cPiece] ^= toBB;       // reset the captured piece
            colorBB[opponentColor(color)] ^= toBB;       // update color bitboard by captured piece
        }

        //occupiedBB            ^=  fromBB;     // update occupied, only from becomes empty
        //emptyBB               ^=  fromBB;     // update empty bitboard
    }

    private int opponentColor(int color) {
        return color == nWhite ? nBlack : nWhite;
    }
}
