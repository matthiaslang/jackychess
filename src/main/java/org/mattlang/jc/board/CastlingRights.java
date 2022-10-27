package org.mattlang.jc.board;

import java.util.Objects;

public class CastlingRights {

    public static final byte WHITE_LONG_MASK = (byte) (1 << 0);
    public static final byte WHITE_SHORT_MASK = (byte) (1 << 1);
    public static final byte BLACK_LONG_MASK = (byte) (1 << 2);
    public static final byte BLACK_SHORT_MASK = (byte) (1 << 3);
    public static final int ALL_CASTLING_RIGHTS =
            WHITE_LONG_MASK | WHITE_SHORT_MASK | BLACK_LONG_MASK | BLACK_SHORT_MASK;

    private byte castlingRights = ALL_CASTLING_RIGHTS;

    public CastlingRights() {
    }

    public CastlingRights(byte castlingRights) {
        this.castlingRights = castlingRights;
    }

    public boolean isAllowed(CastlingType castlingType) {
        int mask = castlingType.getRightsMask();
        return (castlingRights & mask) != 0;
    }

    public void setAllowed(CastlingType castlingType) {
        int mask = castlingType.getRightsMask();
        castlingRights |= mask;
    }

    public void retain(CastlingType castlingType) {
        int mask = castlingType.getRightsMask();
        castlingRights &= 0xFF - mask;
    }

    public byte getRights() {
        return castlingRights;
    }

    public void setRights(byte castlingRights) {
        this.castlingRights = castlingRights;
    }

    public CastlingRights copy() {
        return new CastlingRights(castlingRights);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastlingRights that = (CastlingRights) o;
        return castlingRights == that.castlingRights;
    }

    @Override
    public int hashCode() {
        return Objects.hash(castlingRights);
    }

    /**
     * Are there still any castling rights anyway?
     * If not we can save us some cpu cycles by checking if any right gets lost by a doMove
     *
     * @return
     */
    public boolean hasAnyCastlings() {
        return castlingRights != 0;
    }

    public void clearCastlingRights() {
        castlingRights = 0;
    }

    public void setAllCasltingRights() {
        castlingRights = ALL_CASTLING_RIGHTS;
    }
}
