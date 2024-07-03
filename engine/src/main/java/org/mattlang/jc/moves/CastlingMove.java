package org.mattlang.jc.moves;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.rangeClosed;

import java.util.Objects;

import org.mattlang.jc.board.CastlingType;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.movegenerator.CastlingDef;

import lombok.Getter;

@Getter
public final class CastlingMove {

    /* small castling (g Side) white king target pos.*/
    public static int gWKingTargetPos = Long.numberOfTrailingZeros(BB.G & BB.rank1);
    public static int gWRookTargetPos = Long.numberOfTrailingZeros(BB.F & BB.rank1);

    /* small castling (g Side) black king target pos.*/
    public static int gBKingTargetPos = Long.numberOfTrailingZeros(BB.G & BB.rank8);
    public static int gBRookTargetPos = Long.numberOfTrailingZeros(BB.F & BB.rank8);

    /* long castling (c Side) white king target pos.*/
    public static int cWKingTargetPos = Long.numberOfTrailingZeros(BB.C & BB.rank1);
    public static int cWRookTargetPos = Long.numberOfTrailingZeros(BB.D & BB.rank1);

    /* long castling (c Side) black king target pos.*/
    public static int cBKingTargetPos = Long.numberOfTrailingZeros(BB.C & BB.rank8);
    public static int cBRookTargetPos = Long.numberOfTrailingZeros(BB.D & BB.rank8);

    private final CastlingDef def;

    private final byte kingFrom;

    private final byte kingTo;

    private final byte rookFrom;

    private final byte rookTo;

    public CastlingMove(CastlingDef def, int kingFrom, int kingTo, int rookFrom, int rookTo) {
        this.def = requireNonNull(def);
        this.kingFrom = (byte) kingFrom;
        this.kingTo = (byte) kingTo;
        this.rookFrom = (byte) rookFrom;
        this.rookTo = (byte) rookTo;
    }

    public static CastlingMove createCastlingMove(CastlingType castlingType, int kingFrom, int rookFrom) {
        CastlingDef defintion = createDef(castlingType,
                 kingFrom, castlingType.getKingTargetPos(), rookFrom,
                castlingType.getRookTargetPos());
        return new CastlingMove(defintion, kingFrom,
                castlingType.getKingTargetPos(), rookFrom, castlingType.getRookTargetPos());
    }

    public static CastlingMove createCastlingMove(CastlingType castlingType, int kingFrom,
            int kingTo, int rookFrom, int rookTo) {
        CastlingDef defintion = createDef(castlingType, kingFrom, kingTo, rookFrom, rookTo);
        return new CastlingMove(defintion, kingFrom, kingTo, rookFrom, rookTo);
    }

    private static CastlingDef createDef(CastlingType castlingType,int kingFrom, int kingTo,
            int rookFrom, int rookTo) {
        int minField = min(kingFrom, kingTo, rookFrom, rookTo);
        int maxField = max(kingFrom, kingTo, rookFrom, rookTo);

        int[] fieldPos = rangeClosed(minField, maxField).toArray();

        long emptyMask = 0L;
        Figure[] fieldPosFigs = new Figure[fieldPos.length];
        for (int i = 0; i < fieldPosFigs.length; i++) {
            int pos = fieldPos[i];
            if (pos == kingFrom) {
                fieldPosFigs[i] = castlingType.getColor() == Color.WHITE ? Figure.W_King : Figure.B_King;
            } else if (pos == rookFrom) {
                fieldPosFigs[i] = castlingType.getColor() == Color.WHITE ? Figure.W_Rook : Figure.B_Rook;
            } else {
                fieldPosFigs[i] = Figure.EMPTY;
                emptyMask |= 1L << pos;
            }
        }

        int minKing = Integer.min(kingFrom, kingTo);
        int maxKing = Integer.max(kingFrom, kingTo);
        int[] fieldCheckTst = rangeClosed(minKing, maxKing).toArray();

        long rookFromMask = 1L << rookFrom;
        long kingFromMask = 1L << kingFrom;

        return new CastlingDef(castlingType, fieldPos, fieldPosFigs, fieldCheckTst, rookFromMask, kingFromMask,
                emptyMask);
    }

    private static int min(int... vals) {
        int min = Integer.MAX_VALUE;
        for (int val : vals) {
            if (val < min) {
                min = val;
            }
        }
        return min;
    }

    public static int max(int... vals) {
        int max = Integer.MIN_VALUE;
        for (int i : vals) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CastlingMove that = (CastlingMove) o;
        return kingFrom == that.kingFrom && kingTo == that.kingTo && rookFrom == that.rookFrom
                && rookTo == that.rookTo && def.equals(that.def);
    }

    @Override
    public int hashCode() {
        return Objects.hash(def, kingFrom, kingTo, rookFrom, rookTo);
    }

    public byte getType() {
        return def.getCastlingType().getCastlingMoveType();
    }
}
