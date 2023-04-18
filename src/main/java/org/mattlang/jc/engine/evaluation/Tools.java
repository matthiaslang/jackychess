package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BB.Direction.NORTH;
import static org.mattlang.jc.board.bitboard.BB.Direction.SOUTH;
import static org.mattlang.jc.board.bitboard.BB.File.FILE_H;
import static org.mattlang.jc.board.bitboard.BB.Rank.RANK_8;
import static org.mattlang.jc.board.bitboard.BB.Square.SQ_A8;
import static org.mattlang.jc.board.bitboard.BB.Square.SQ_H1;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;

public class Tools {

    public static final long DarkSquares = 0xAA55AA55AA55AA55L;

    /**
     * precalc distance.
     */
    private static int[][] dist = new int[64][64];

    static {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                dist[i][j] = calcDistance(i, j);
            }
        }
    }

    /**
     * precalc manhattan distance.
     */
    private static int[][] manDist = new int[64][64];

    static {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                manDist[i][j] = calcManhattanDistance(i, j);
            }
        }
    }

    public static final int rankOf(int sq) {
        return sq >> 3;
    }

    public static final int fileOf(int sq) {
        return sq & 7;
    }

    public static final int edgeFileDistance(int f) {
        return Math.min(f, FILE_H.ordinal() - f);
    }

    public static final int edgeRankDistance(int r) {
        return Math.min(r, RANK_8.ordinal() - r);
    }

    public static final int distance(int sq1, int sq2) {
        return dist[sq1][sq2];
    }

    public static final int calcDistance(int sq1, int sq2) {
        return Math.max(rankDistance(sq1, sq2), fileDistance(sq1, sq2));
    }

    public static final int rankDistance(int sq1, int sq2) {
        int rank1, rank2;
        rank1 = sq1 >> 3;
        rank2 = sq2 >> 3;
        return Math.abs(rank2 - rank1);
    }

    public static final int fileDistance(int sq1, int sq2) {
        int file1, file2;
        file1 = sq1 & 7;
        file2 = sq2 & 7;
        return Math.abs(file2 - file1);
    }

    public static final int manhattanDistance(int sq1, int sq2) {
        return manDist[sq1][sq2];
    }

    public static final int calcManhattanDistance(int sq1, int sq2) {
        int file1, file2, rank1, rank2;
        int rankDistance, fileDistance;
        file1 = sq1 & 7;
        file2 = sq2 & 7;
        rank1 = sq1 >> 3;
        rank2 = sq2 >> 3;
        rankDistance = Math.abs(rank2 - rank1);
        fileDistance = Math.abs(file2 - file1);
        return rankDistance + fileDistance;
    }

    public static final int colDistance(int sq1, int sq2) {
        int file1, file2;
        file1 = sq1 & 7;
        file2 = sq2 & 7;
        return Math.abs(file2 - file1);
    }

    public static final int calcKnightDistance(long b1, long b2) {
        int d = 0;
        while ((b1 & b2) == 0) {
            b1 = BB.knightAttacks(b1); // as long as sets are disjoint
            d++; // increment distance
        }
        return d;
    }

    public static final int knightDistance(int a, int b) {
        return calcKnightDistance(1L << a, 1L << b);
    }
    //

    // Used to drive the king towards the edge of the board
    // in KX vs K and KQ vs KR endgames.
    // Values range from 27 (center squares) to 90 (in the corners)
    public static int push_to_edge(int s) {
        int rd = edgeRankDistance(rankOf(s)), fd = edgeFileDistance(fileOf(s));
        return 90 - (7 * fd * fd / 2 + 7 * rd * rd / 2);
    }

    // Used to drive the king towards A1H8 corners in KBN vs K endgames.
    // Values range from 0 on A8H1 diagonal to 7 in A1H8 corners
    public static int push_to_corner(int s) {
        return Math.abs(7 - rankOf(s) - fileOf(s));
    }

    // Drive a piece close to or away from another piece
    public static int push_close(int s1, int s2) {
        return 140 - 20 * calcDistance(s1, s2);
    }

    public static int push_away(int s1, int s2) {
        return 120 - push_close(s1, s2);
    }

    public static boolean opposite_colors(int s1, int s2) {
        return ((s1 + rankOf(s1) + s2 + rankOf(s2)) & 1) != 0;
    }

    public static int flip_rank(int s) { // Swap A1 <-> A8
        return s ^ SQ_A8.ordinal();
    }

    public static int flip_file(int s) { // Swap A1 <-> H1
        return s ^ SQ_H1.ordinal();
    }

    public static int relativeRank(int c, BB.Rank r) {
        return r.ordinal() ^ (c * 7);
    }

    public static int relativeRank(Color c, BB.Rank r) {
        return relativeRank(c.ordinal(), r);
    }

    public static int relativeRank(int c, int s) {
        return rankOf(s) ^ (c * 7);
    }

    public static int relativeRank(Color c, int s) {
        return rankOf(s) ^ (c.ordinal() * 7);
    }

    public static int makeSquare(BB.File f, BB.Rank r) {
        return (r.ordinal() << 3) + f.ordinal();
    }

    public static int makeSquare(int f, int r) {
        return (r << 3) + f;
    }

    public static long forward_file_bb(int c, int s) {
        return forward_ranks_bb(c, s) & file_bb_ofSquare(s);
    }

    public static long forward_file_bb(Color c, int s) {
        return forward_ranks_bb(c, s) & file_bb_ofSquare(s);
    }

    public static long file_bb(int f) {
        return BB.A << f;
    }

    public static long file_bb_ofSquare(int s) {
        return file_bb(fileOf(s));
    }

    /// forward_ranks_bb() returns a bitboard representing the squares on the ranks in
    /// front of the given one, from the point of view of the given color. For instance,
    /// forward_ranks_bb(BLACK, SQ_D3) will return the 16 squares on ranks 1 and 2.

    public static long forward_ranks_bb(Color c, int s) {
        return c == WHITE ? ~BB.rank1 << 8 * relativeRank(WHITE, s)
                : ~BB.rank8 >> 8 * relativeRank(BLACK, s);
    }

    public static long forward_ranks_bb(int c, int s) {
        return c == WHITE.ordinal() ? ~BB.rank1 << 8 * relativeRank(WHITE, s)
                : ~BB.rank8 >> 8 * relativeRank(BLACK, s);
    }

    public static BB.Direction pawn_push(Color c) {
        return c == WHITE ? NORTH : SOUTH;
    }

    public static BB.Direction pawn_push(int c) {
        return c == WHITE.ordinal() ? NORTH : SOUTH;
    }
}
