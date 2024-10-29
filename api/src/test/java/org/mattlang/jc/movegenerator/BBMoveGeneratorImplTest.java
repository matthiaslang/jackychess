package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.BB.soutOne;

import org.junit.Test;
import org.mattlang.jc.board.BB;

public class BBMoveGeneratorImplTest {

    @Test
    public void bit63Problem() {
        long kingSet = 1L << 63;
        long attacks = soutOne(kingSet);


        System.out.println("king  \n" + BB.toStrBoard(attacks));
    }

    @Test
    public void testprecalculatedArrays() {

        final long[] kingAttacks = new long[64];
        final long[] knightAttacks = new long[64];

        // precalculate attacks:

        for (int sq = 0; sq < 64; sq++) {
            long sqBB = 1L << sq;
            kingAttacks[sq] = BB.kingAttacks(sqBB);
            knightAttacks[sq] = BB.knightAttacks(sqBB);
        }

        for (int sq = 0; sq < 64; sq++) {
            System.out.println("king pos " + sq + "\n" + BB.toStrBoard(1L << sq));
            System.out.println("king pos " + sq + "\n" + BB.toStrBoard(kingAttacks[sq]));

        }
        for (int sq = 0; sq < 64; sq++) {
            System.out.println("knight pos " + sq + "\n" + BB.toStrBoard(knightAttacks[sq]));

        }
    }
}