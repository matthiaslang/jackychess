package org.mattlang.jc.moves;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.rangeClosed;

import java.util.Objects;

import org.mattlang.jc.board.CastlingType;
import org.mattlang.jc.movegenerator.CastlingDef;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public final class CastlingMove {

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

    private static CastlingDef createDef(CastlingType castlingType, int kingFrom, int kingTo,
            int rookFrom, int rookTo) {
        int minField = min(kingFrom, kingTo, rookFrom, rookTo);
        int maxField = max(kingFrom, kingTo, rookFrom, rookTo);

        long emptyMask = 0L;
        for (int pos = minField; pos <= maxField; pos++) {
            if (pos != kingFrom && pos != rookFrom) {
                emptyMask |= 1L << pos;
            }
        }

        int minKing = Integer.min(kingFrom, kingTo);
        int maxKing = Integer.max(kingFrom, kingTo);
        int[] fieldCheckTst = rangeClosed(minKing, maxKing).toArray();

        long rookFromMask = 1L << rookFrom;
        long kingFromMask = 1L << kingFrom;

        return new CastlingDef(castlingType, fieldCheckTst, rookFromMask, kingFromMask,
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
