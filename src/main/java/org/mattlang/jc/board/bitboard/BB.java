package org.mattlang.jc.board.bitboard;

import java.util.Arrays;

import org.mattlang.jc.board.BoardPrinter;

import lombok.Getter;

/**
 * Static bitboard stuff.
 */
public class BB {

    /**
     * all squares numbered starting with 0 as enums:
     */
    public enum Square {
        SQ_A1, SQ_B1, SQ_C1, SQ_D1, SQ_E1, SQ_F1, SQ_G1, SQ_H1,
        SQ_A2, SQ_B2, SQ_C2, SQ_D2, SQ_E2, SQ_F2, SQ_G2, SQ_H2,
        SQ_A3, SQ_B3, SQ_C3, SQ_D3, SQ_E3, SQ_F3, SQ_G3, SQ_H3,
        SQ_A4, SQ_B4, SQ_C4, SQ_D4, SQ_E4, SQ_F4, SQ_G4, SQ_H4,
        SQ_A5, SQ_B5, SQ_C5, SQ_D5, SQ_E5, SQ_F5, SQ_G5, SQ_H5,
        SQ_A6, SQ_B6, SQ_C6, SQ_D6, SQ_E6, SQ_F6, SQ_G6, SQ_H6,
        SQ_A7, SQ_B7, SQ_C7, SQ_D7, SQ_E7, SQ_F7, SQ_G7, SQ_H7,
        SQ_A8, SQ_B8, SQ_C8, SQ_D8, SQ_E8, SQ_F8, SQ_G8, SQ_H8,
    }

    public enum File {
        FILE_A, FILE_B, FILE_C, FILE_D, FILE_E, FILE_F, FILE_G, FILE_H;

        private static final File[] FILES = File.values();

        public static File file(int fileOrdinal) {
            return FILES[fileOrdinal];
        }
    }

    public enum Rank {
        RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8;

        private static final Rank[] RANKS = Rank.values();

        public static Rank rank(int fileOrdinal) {
            return RANKS[fileOrdinal];
        }
    }

    public enum Direction{
        NORTH(8),
        EAST(1),
        SOUTH(-8),
        WEST(-1),

        NORTH_EAST(NORTH, EAST),
        SOUTH_EAST(SOUTH, EAST),
        SOUTH_WEST(SOUTH, WEST),
        NORTH_WEST(NORTH, WEST);

        @Getter
        private final int offset;

        Direction(int offset) {
            this.offset = offset;
        }

        Direction(Direction...other){
            offset = Arrays.stream(other).map(d->d.offset).reduce(Integer::sum).orElse(0);
        }
    }

    // rank 1
    public static final long H1 = 1L;
    public static final long G1 = H1 << 1;
    public static final long F1 = G1 << 1;
    public static final long E1 = F1 << 1;
    public static final long D1 = E1 << 1;
    public static final long C1 = D1 << 1;
    public static final long B1 = C1 << 1;
    public static final long A1 = B1 << 1;

    // rank 2
    public static final long H2 = A1 << 1;
    public static final long G2 = H2 << 1;
    public static final long F2 = G2 << 1;
    public static final long E2 = F2 << 1;
    public static final long D2 = E2 << 1;
    public static final long C2 = D2 << 1;
    public static final long B2 = C2 << 1;
    public static final long A2 = B2 << 1;

    // rank 3
    public static final long H3 = A2 << 1;
    public static final long G3 = H3 << 1;
    public static final long F3 = G3 << 1;
    public static final long E3 = F3 << 1;
    public static final long D3 = E3 << 1;
    public static final long C3 = D3 << 1;
    public static final long B3 = C3 << 1;
    public static final long A3 = B3 << 1;

    // rank 4
    public static final long H4 = A3 << 1;
    public static final long G4 = H4 << 1;
    public static final long F4 = G4 << 1;
    public static final long E4 = F4 << 1;
    public static final long D4 = E4 << 1;
    public static final long C4 = D4 << 1;
    public static final long B4 = C4 << 1;
    public static final long A4 = B4 << 1;

    // rank 5
    public static final long H5 = A4 << 1;
    public static final long G5 = H5 << 1;
    public static final long F5 = G5 << 1;
    public static final long E5 = F5 << 1;
    public static final long D5 = E5 << 1;
    public static final long C5 = D5 << 1;
    public static final long B5 = C5 << 1;
    public static final long A5 = B5 << 1;

    // rank 6
    public static final long H6 = A5 << 1;
    public static final long G6 = H6 << 1;
    public static final long F6 = G6 << 1;
    public static final long E6 = F6 << 1;
    public static final long D6 = E6 << 1;
    public static final long C6 = D6 << 1;
    public static final long B6 = C6 << 1;
    public static final long A6 = B6 << 1;

    // rank 7
    public static final long H7 = A6 << 1;
    public static final long G7 = H7 << 1;
    public static final long F7 = G7 << 1;
    public static final long E7 = F7 << 1;
    public static final long D7 = E7 << 1;
    public static final long C7 = D7 << 1;
    public static final long B7 = C7 << 1;
    public static final long A7 = B7 << 1;

    // rank 8
    public static final long H8 = A7 << 1;
    public static final long G8 = H8 << 1;
    public static final long F8 = G8 << 1;
    public static final long E8 = F8 << 1;
    public static final long D8 = E8 << 1;
    public static final long C8 = D8 << 1;
    public static final long B8 = C8 << 1;
    public static final long A8 = B8 << 1;


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

    public static final long[][] IN_BETWEEN = new long[64][64];

    static {
        // precalculate attacks:
        long sqBB = 1;
        for (int sq = 0; sq < 64; sq++, sqBB <<= 1) {
            kingAttacks[sq] = BB.kingAttacks(sqBB);
            knightAttacks[sq] = BB.knightAttacks(sqBB);
        }

        fillInbetweens();
    }

    private static void fillInbetweens() {
        int i;

        // fill from->to where to > from
        for (int from = 0; from < 64; from++) {
            for (int to = from + 1; to < 64; to++) {

                // horizontal
                if (from / 8 == to / 8) {
                    i = to - 1;
                    while (i > from) {
                        IN_BETWEEN[from][to] |= 1L << i;
                        i--;
                    }
                }

                // vertical
                if (from % 8 == to % 8) {
                    i = to - 8;
                    while (i > from) {
                        IN_BETWEEN[from][to] |= 1L << i;
                        i -= 8;
                    }
                }

                // diagonal \
                if ((to - from) % 9 == 0 && to % 8 > from % 8) {
                    i = to - 9;
                    while (i > from) {
                        IN_BETWEEN[from][to] |= 1L << i;
                        i -= 9;
                    }
                }

                // diagonal /
                if ((to - from) % 7 == 0 && to % 8 < from % 8) {
                    i = to - 7;
                    while (i > from) {
                        IN_BETWEEN[from][to] |= 1L << i;
                        i -= 7;
                    }
                }
            }
        }

        // fill from->to where to < from
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < from; to++) {
                IN_BETWEEN[from][to] = IN_BETWEEN[to][from];
            }
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
        gen |= (gen >>> 8);
        gen |= (gen >>> 16);
        gen |= (gen >>> 32);
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

    /**
     * Flip a bitboard vertically about the centre ranks.
     * Rank 1 is mapped to rank 8 and vice versa.
     *
     * @param x any bitboard
     * @return bitboard x flipped vertically
     */
    public static long flipVertical(long x) {
        return ((x << 56)) |
                ((x << 40) & 0x00ff000000000000L) |
                ((x << 24) & 0x0000ff0000000000L) |
                ((x << 8) & 0x000000ff00000000L) |
                ((x >>> 8) & 0x00000000ff000000L) |
                ((x >>> 24) & 0x0000000000ff0000L) |
                ((x >>> 40) & 0x000000000000ff00L) |
                ((x >>> 56));
    }

    public static long least_significant_square_bb(long bb) {
        long least_significant = bb & -bb;
        return least_significant;
    }
}
