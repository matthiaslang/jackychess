package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.bitboard.BB;

public class Tools {


    public static final int manhattanDistance(int sq1, int sq2) {
        int file1, file2, rank1, rank2;
        int rankDistance, fileDistance;
        file1 = sq1  & 7;
        file2 = sq2  & 7;
        rank1 = sq1 >> 3;
        rank2 = sq2 >> 3;
        rankDistance = Math.abs (rank2 - rank1);
        fileDistance = Math.abs (file2 - file1);
        return rankDistance + fileDistance;
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

}
