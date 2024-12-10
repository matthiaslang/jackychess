package org.mattlang.jc.board;

import static org.mattlang.jc.board.Color.WHITE;

import java.util.Arrays;

import lombok.Getter;

/**
 * Static bitboard stuff.
 */
public class BB {

    public static long fileFromChar(char ch) {
        switch (Character.toUpperCase(ch)) {
            case 'A': return A;
            case 'B': return B;
            case 'C': return C;
            case 'D': return D;
            case 'E': return E;
            case 'F': return F;
            case 'G': return G;
            case 'H': return H;
        }
        throw new IllegalStateException("cant map file from char sym!");
    }


    public enum Direction {
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

        Direction(Direction... other) {
            offset = Arrays.stream(other).map(d -> d.offset).reduce(Integer::sum).orElse(0);
        }
    }

    public static final long A1 = 1L << Square.SQ_A1.ordinal();
    public static final long A2 = 1L << Square.SQ_A2.ordinal();
    public static final long A3 = 1L << Square.SQ_A3.ordinal();
    public static final long A4 = 1L << Square.SQ_A4.ordinal();
    public static final long A5 = 1L << Square.SQ_A5.ordinal();
    public static final long A6 = 1L << Square.SQ_A6.ordinal();
    public static final long A7 = 1L << Square.SQ_A7.ordinal();
    public static final long A8 = 1L << Square.SQ_A8.ordinal();
    public static final long B1 = 1L << Square.SQ_B1.ordinal();
    public static final long B2 = 1L << Square.SQ_B2.ordinal();
    public static final long B3 = 1L << Square.SQ_B3.ordinal();
    public static final long B4 = 1L << Square.SQ_B4.ordinal();
    public static final long B5 = 1L << Square.SQ_B5.ordinal();
    public static final long B6 = 1L << Square.SQ_B6.ordinal();
    public static final long B7 = 1L << Square.SQ_B7.ordinal();
    public static final long B8 = 1L << Square.SQ_B8.ordinal();
    public static final long C1 = 1L << Square.SQ_C1.ordinal();
    public static final long C2 = 1L << Square.SQ_C2.ordinal();
    public static final long C3 = 1L << Square.SQ_C3.ordinal();
    public static final long C4 = 1L << Square.SQ_C4.ordinal();
    public static final long C5 = 1L << Square.SQ_C5.ordinal();
    public static final long C6 = 1L << Square.SQ_C6.ordinal();
    public static final long C7 = 1L << Square.SQ_C7.ordinal();
    public static final long C8 = 1L << Square.SQ_C8.ordinal();
    public static final long D1 = 1L << Square.SQ_D1.ordinal();
    public static final long D2 = 1L << Square.SQ_D2.ordinal();
    public static final long D3 = 1L << Square.SQ_D3.ordinal();
    public static final long D4 = 1L << Square.SQ_D4.ordinal();
    public static final long D5 = 1L << Square.SQ_D5.ordinal();
    public static final long D6 = 1L << Square.SQ_D6.ordinal();
    public static final long D7 = 1L << Square.SQ_D7.ordinal();
    public static final long D8 = 1L << Square.SQ_D8.ordinal();
    public static final long E1 = 1L << Square.SQ_E1.ordinal();
    public static final long E2 = 1L << Square.SQ_E2.ordinal();
    public static final long E3 = 1L << Square.SQ_E3.ordinal();
    public static final long E4 = 1L << Square.SQ_E4.ordinal();
    public static final long E5 = 1L << Square.SQ_E5.ordinal();
    public static final long E6 = 1L << Square.SQ_E6.ordinal();
    public static final long E7 = 1L << Square.SQ_E7.ordinal();
    public static final long E8 = 1L << Square.SQ_E8.ordinal();
    public static final long F1 = 1L << Square.SQ_F1.ordinal();
    public static final long F2 = 1L << Square.SQ_F2.ordinal();
    public static final long F3 = 1L << Square.SQ_F3.ordinal();
    public static final long F4 = 1L << Square.SQ_F4.ordinal();
    public static final long F5 = 1L << Square.SQ_F5.ordinal();
    public static final long F6 = 1L << Square.SQ_F6.ordinal();
    public static final long F7 = 1L << Square.SQ_F7.ordinal();
    public static final long F8 = 1L << Square.SQ_F8.ordinal();
    public static final long G1 = 1L << Square.SQ_G1.ordinal();
    public static final long G2 = 1L << Square.SQ_G2.ordinal();
    public static final long G3 = 1L << Square.SQ_G3.ordinal();
    public static final long G4 = 1L << Square.SQ_G4.ordinal();
    public static final long G5 = 1L << Square.SQ_G5.ordinal();
    public static final long G6 = 1L << Square.SQ_G6.ordinal();
    public static final long G7 = 1L << Square.SQ_G7.ordinal();
    public static final long G8 = 1L << Square.SQ_G8.ordinal();
    public static final long H1 = 1L << Square.SQ_H1.ordinal();
    public static final long H2 = 1L << Square.SQ_H2.ordinal();
    public static final long H3 = 1L << Square.SQ_H3.ordinal();
    public static final long H4 = 1L << Square.SQ_H4.ordinal();
    public static final long H5 = 1L << Square.SQ_H5.ordinal();
    public static final long H6 = 1L << Square.SQ_H6.ordinal();
    public static final long H7 = 1L << Square.SQ_H7.ordinal();
    public static final long H8 = 1L << Square.SQ_H8.ordinal();

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

    public static final long LEFT_FLANK = A | B | C | D;
    public static final long RIGHT_FLANK = E | F | G | H;

    public static final long rank1 = 0x00000000000000FFL;
    public static final long rank2 = 0x000000000000FF00L;
    public static final long rank3 = 0x0000000000FF0000L;
    public static final long rank4 = 0x00000000FF000000L;
    public static final long rank5 = 0x000000FF00000000L;
    public static final long rank6 = 0x0000FF0000000000L;
    public static final long rank7 = 0x00FF000000000000L;
    public static final long rank8 = 0xFF00000000000000L;

    public static final long rank12 = rank1 | rank2;
    public static final long rank78 = rank7 | rank8;

    public static final long notAFile = ~A;
    public static final long notHFile = ~H;

    public static final long ABC_File = A | B | C;
    public static final long ABC_on_rank2 = ABC_File & rank2;
    public static final long ABC_on_rank3 = ABC_File & rank3;

    public static final long FGH_File = F | G | H;

    public static final long FGH_on_rank2 = FGH_File & rank2;
    public static final long FGH_on_rank3 = FGH_File & rank3;

    public static final long CenterFiles = C | D | E | F;

    public static final long WHITE_SQUARES = 0xaa55aa55aa55aa55L;
    public static final long BLACK_SQUARES = ~WHITE_SQUARES;

    private static final long[] kingAttacks = new long[64];
    private static final long[] knightAttacks = new long[64];

    public static final long[][] IN_BETWEEN = new long[64][64];

    public static final long ADJACENT_FILES[] = {
            B,
            C | A,
            D | B,
            E | C,
            F | D,
            G | E,
            H | F,
            G,
    };

    public static final int FILE_NB = 8;

    private static int[][] KING_PAWN_FILE_DISTANCE = new int[FILE_NB][1 << FILE_NB];

    static {
        // precalculate attacks:
        long sqBB = 1;
        for (int sq = 0; sq < 64; sq++, sqBB <<= 1) {
            kingAttacks[sq] = BB.kingAttacks(sqBB);
            knightAttacks[sq] = BB.knightAttacks(sqBB);
        }

        fillInbetweens();

        fillPawnKingFileDistance();
    }


    private static void fillPawnKingFileDistance() {
        // Init a table to compute the distance between Pawns and Kings file-wise
        for (int mask = 0; mask <= 0xFF; mask++) {
            for (int file = 0; file < FILE_NB; file++) {

                int leftDist = FILE_NB - 1;
                for (int i = 0; i < file; i++) {
                    if ((mask & (1 << i)) != 0) {
                        leftDist = file - i;
                    }
                }
                int rightDist = FILE_NB - 1;
                for (int i = file; i >= 0; i--) {
                    if ((mask & (1 << i)) != 0) {
                        rightDist = file - i;
                    }
                }
                int dist = Math.min(rightDist, leftDist);

                KING_PAWN_FILE_DISTANCE[file][mask] = dist;
            }
        }
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

    public static final long getKingAttacs(int kingPos) {
        return kingAttacks[kingPos];
    }

    public static final long getKnightAttacs(int kingPos) {
        return knightAttacks[kingPos];
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

    /**
     * returns the squares attacked by pawns of the given color  from the squares in the given bitboard.
     */
    public static long pawnAttacks(Color color, long b) {
        return color == WHITE ? noWeOne(b) | noEaOne(b) : soWeOne(b) | soEaOne(b);
    }

    public static String toStrBoard(long bb) {
        return BoardPrinterUtil.toStr((row, col) -> {
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

    /**
     * Returns the number of open files of a pawn mask.
     *
     * This is calculated by front filling the pask to the first rank and "anding" with the inverse front filled mask.
     *
     * @param pawns
     * @return
     */
    public static int openFileCount(long pawns) {
        // front fill till, inverse to get free fields anding with rank1 gives the number of free files:
        long southFilled = southFill(pawns);
        long freeFields = ~southFilled;
        long freeOnFirstRank = freeFields & rank1;
        return Long.bitCount(freeOnFirstRank);
    }

    public static int kingPawnFileDistance(long pawns, int ksq) {
        long southFilled = southFill(pawns);
        return KING_PAWN_FILE_DISTANCE[Tools.fileOf(ksq)][(int) (southFilled & rank1)];
    }

    public static long wFrontFill(long wpawns) {
        return nortFill(wpawns);
    }

    public static long wFrontSpans(long wpawns) {
        return nortOne(nortFill(wpawns));
    }

    public static long bFrontSpans(long bpawns) {
        return soutOne(southFill(bpawns));
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
