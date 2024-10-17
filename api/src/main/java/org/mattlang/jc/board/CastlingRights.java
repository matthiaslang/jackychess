package org.mattlang.jc.board;

import java.util.Objects;

public class CastlingRights {

    public static final byte INITIAL_RIGHTS = 0b1111;
    private byte castlingRights = INITIAL_RIGHTS;

    public CastlingRights() {
    }

    public CastlingRights(byte castlingRights) {
        this.castlingRights = castlingRights;
    }

    public boolean isAllowed(CastlingType castlingType) {
        return isAllowed(castlingRights, castlingType);
    }

    public static boolean isAllowed(byte castlingRights, CastlingType castlingType) {
        return (castlingRights & castlingType.getCastlingBitMask()) != 0;
    }

    public void setAllowed(CastlingType castlingType) {
        castlingRights |= castlingType.getCastlingBitMask();
    }

    public void removeRight(CastlingType castlingType) {
        castlingRights &= 0xFF - castlingType.getCastlingBitMask();
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
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
        castlingRights = 0b1111;
    }
}
