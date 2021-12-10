package org.mattlang.jc.board.bitboard;

import org.mattlang.jc.board.BoardPrinter;

/**
 * Static bitboard stuff.
 */
public class BB {

    // the individual files:
    public static final long A = 0x0101010101010101L;
    public static final long B = 0x0202020202020202L;
    public static final long C = 0x0404040404040404L;
    public static final long D = 0x0808080808080808L;
    public static final long E = 0x1010101010101010L;
    public static final long F = 0x2020202020202020L;
    public static final long G = 0x4040404040404040L;
    public static final long H = 0x8080808080808080L;

    public static final long ALL = A | B | C | D | E | F | G | H;

    public static final long rank1 = 0x00000000000000FFL;
    public static final long rank2 = 0x000000000000FF00L;
    public static final long rank3 = 0x0000000000FF0000L;
    public static final long rank4 = 0x00000000FF000000L;
    public static final long rank5 = 0x000000FF00000000L;
    public static final long rank6 = 0x0000FF0000000000L;
    public static final long rank7 = 0x00FF000000000000L;
    public static final long rank8 = 0xFF00000000000000L;

    public static final long notAFile = ALL & ~A;
    public static final long notHFile = ALL & ~H;

    public static final long ABC_File = A | B | C;
    public static final long ABC_on_rank2 = ABC_File & rank2;
    public static final long ABC_on_rank3 = ABC_File & rank3;
    public static final long ABC_on_rank7 = ABC_File & rank7;
    public static final long ABC_on_rank6 = ABC_File & rank6;

    public static final long FGH_File = F | G | H;

    public static final long FGH_on_rank2 = FGH_File & rank2;
    public static final long FGH_on_rank3 = FGH_File & rank3;
    public static final long FGH_on_rank7 = FGH_File & rank7;
    public static final long FGH_on_rank6 = FGH_File & rank6;

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

    /**
     * Calc weighted population count; useful in evaluations.
     *
     * @param bb
     * @param weights
     * @return
     */
    public static int dotProduct(long bb, byte weights[]) {
        long bit = 1;
        int accu = 0;
        for (int sq = 0; sq < 64; sq++, bit += bit) {
            if ((bb & bit) != 0)
                accu += weights[sq];
            // accu += weights[sq] & -((  bb & bit) == bit); // branchless 1
            // accu += weights[sq] & -(( ~bb & bit) == 0);   // branchless 2
        }
        return accu;
    }

    // pawn file fills, see: https://www.chessprogramming.org/Pawn_Fills#FileFill
    

    public static long nortFill(long gen) {
        gen |= (gen << 8);
        gen |= (gen << 16);
        gen |= (gen << 32);
        return gen;
    }

    public static long southFill(long gen) {
        gen |= (gen >> 8);
        gen |= (gen >> 16);
        gen |= (gen >> 32);
        return gen;
    }

    public static long wFrontFill(long wpawns) {
        return nortFill(wpawns);
    }

    public static long wRearFill(long wpawns) {
        return southFill(wpawns);
    }

    public static long bFrontFill(long bpawns) {
        return southFill(bpawns);
    }

    public static long bRearFill(long bpawns) {
        return nortFill(bpawns);
    }

    public static long fileFill(long gen) {
        return nortFill(gen) | southFill(gen);
    }
}
