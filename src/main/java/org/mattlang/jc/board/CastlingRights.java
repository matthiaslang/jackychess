package org.mattlang.jc.board;

import static org.mattlang.jc.board.RochadeType.SHORT;

import java.util.Objects;

public class CastlingRights {

    private byte castlingRights = 0b1111;

    public CastlingRights() {
    }

    public CastlingRights(byte castlingRights) {
        this.castlingRights = castlingRights;
    }

    public boolean isAllowed(Color color, RochadeType type) {
        int allowedMask = createMask(color, type);
        return (castlingRights & allowedMask) != 0;
    }

    public void setAllowed(Color color, RochadeType type) {
        int mask = createMask(color, type);
        castlingRights |= mask;
    }

    public void setAllowed(CastlingType castlingType) {
        int mask = createMask(castlingType.getColor(), castlingType.getRochadeType());
        castlingRights |= mask;
    }

    public void retain(Color color, RochadeType type) {
        int mask = createMask(color, type);
        castlingRights &= 0xFF-mask;
    }

    private byte createMask(Color color, RochadeType type) {
        int idx = type == SHORT ? 0 : 1;
        if (color == Color.BLACK) {
            idx += 2;
        }
        byte allowedMask = (byte) (1 << idx);
        return allowedMask;
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
}
