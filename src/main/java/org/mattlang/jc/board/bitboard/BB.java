package org.mattlang.jc.board.bitboard;

import org.mattlang.jc.board.BoardPrinter;

/**
 * Static bitboard stuff.
 */
public class BB {

    public static final long rank4 = 0x00000000FF000000L;
    public static final long rank5 = 0x000000FF00000000L;

    public static final long notAFile = 0xfefefefefefefefeL;
    public static final long notHFile = 0x7f7f7f7f7f7f7f7fL;

    private static final long[] kingAttacks = new long[64];
    private static final long[] knightAttacks = new long[64];

    static {
        // precalculate attacks:
        long sqBB = 1;
        for (int sq = 0; sq < 64; sq++, sqBB <<= 1) {
            kingAttacks[sq] = BB.kingAttacks(sqBB);
            knightAttacks[sq] = BB.knightAttacks(sqBB);
        }
    }


    public static final long getKingAttacs(int kingPos){
        return kingAttacks[kingPos] ;
    }

    public static final long getKnightAttacs(int kingPos){
        return knightAttacks[kingPos] ;
    }

    public static final long soutOne(long b) {
        return b >>> 8;
    }

    public static final long nortOne(long b) {
        return b << 8;
    }



    public static final long eastOne(long b) {
        return (b << 1) & notAFile;
    }

    public static final long noEaOne(long b) {
        return (b << 9) & notAFile;
    }

    public static final long soEaOne(long b) {
        return (b >>> 7) & notAFile;
    }

    public static final long westOne(long b) {
        return (b >>> 1) & notHFile;
    }

    public static final long soWeOne(long b) {
        return (b >>> 9) & notHFile;
    }

    public static final long noWeOne(long b) {
        return (b << 7) & notHFile;
    }

    public static final long kingAttacks(long kingSet) {
        return eastOne(kingSet) | westOne(kingSet) | nortOne(kingSet) | soutOne(kingSet)
                | noEaOne(kingSet) | noWeOne(kingSet) | soEaOne(kingSet) | soWeOne(kingSet);
    }

    public static long knightAttacks(long knightSet) {
        return nortOne(noEaOne(knightSet))
                | nortOne(noWeOne(knightSet))
                | westOne(noWeOne(knightSet))
                | westOne(soWeOne(knightSet))
                | eastOne(noEaOne(knightSet))
                | eastOne(soEaOne(knightSet))
                | soutOne(soWeOne(knightSet))
                | soutOne(soEaOne(knightSet));
    }

    public static long wSinglePushTargets(long wpawns, long empty) {
        return nortOne(wpawns) & empty;
    }

    public static long wDblPushTargets(long wpawns, long empty) {
        long singlePushs = wSinglePushTargets(wpawns, empty);
        return nortOne(singlePushs) & empty & rank4;
    }

    public static long bSinglePushTargets(long bpawns, long empty) {
        return soutOne(bpawns) & empty;
    }

    public static long bDoublePushTargets(long bpawns, long empty) {
        long singlePushs = bSinglePushTargets(bpawns, empty);
        return soutOne(singlePushs) & empty & rank5;
    }

    public static long wPawnsAble2Push(long wpawns, long empty) {
        return soutOne(empty) & wpawns;
    }

    public static long wPawnsAble2DblPush(long wpawns, long empty) {
        long emptyRank3 = soutOne(empty & rank4) & empty;
        return wPawnsAble2Push(wpawns, emptyRank3);
    }

    public static long wPawnEastAttacks(long wpawns) {
        return noEaOne(wpawns);
    }

    public static long wPawnWestAttacks(long wpawns) {
        return noWeOne(wpawns);
    }

    public static long bPawnEastAttacks(long bpawns) {
        return soEaOne(bpawns);
    }

    public static long bPawnWestAttacks(long bpawns) {
        return soWeOne(bpawns);
    }


    public static String toStrBoard(long bb) {
        return BoardPrinter.toStr((row, col) -> {
            int pos = (7 - row) * 8 + col;
            return isBitSet(bb, pos) ? 'X' : '.';
        });

    }

    public static String toStr(long bb) {
        StringBuilder b = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int pos = (7 - row) * 8 + col;
                b.append(isBitSet(bb, pos) ? '1' : '.');

            }
            b.append("\n");
        }
        return b.toString();
    }

    private static boolean isBitSet(long bb, int pos) {
        long posMask = 1L << pos;
        return (bb & posMask) != 0;
    }
}
