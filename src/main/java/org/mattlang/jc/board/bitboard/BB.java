package org.mattlang.jc.board.bitboard;

/**
 * Static bitboard stuff.
 */
public class BB {

    public static final long soutOne(long b) {
        return b >> 8;
    }

    public static final long nortOne(long b) {
        return b << 8;
    }

    public static final long notAFile = 0xfefefefefefefefeL;
    public static final long notHFile = 0x7f7f7f7f7f7f7f7fL;

    public static final long eastOne(long b) {
        return (b << 1) & notAFile;
    }

    public static final long noEaOne(long b) {
        return (b << 9) & notAFile;
    }

    public static final long soEaOne(long b) {
        return (b >> 7) & notAFile;
    }

    public static final long westOne(long b) {
        return (b >> 1) & notHFile;
    }

    public static final long soWeOne(long b) {
        return (b >> 9) & notHFile;
    }

    public static final long noWeOne(long b) {
        return (b << 7) & notHFile;
    }

    public static final long kingAttacks(long kingSet) {
        long attacks = eastOne(kingSet) | westOne(kingSet) | nortOne(kingSet) | soutOne(kingSet)
                | noEaOne(kingSet) | noWeOne(kingSet) | soEaOne(kingSet | soWeOne(kingSet));
//        kingSet |= attacks;
//        attacks |= nortOne(kingSet) | soutOne(kingSet);
        return attacks;
    }
}
