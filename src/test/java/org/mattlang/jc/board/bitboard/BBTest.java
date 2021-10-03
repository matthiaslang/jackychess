package org.mattlang.jc.board.bitboard;

import org.junit.Test;

public class BBTest {

    @Test
    public void testKingAttacks() {

        for (int i = 0; i < 64; i++) {
            System.out.println("King pos \n" + BitChessBoard.toStr(1L << i));
            System.out.println("Attacks: \n" + BitChessBoard.toStr(BB.kingAttacks(1L << i)));
        }

    }

    @Test
    public void southOne() {
        for (int i = 0; i < 64; i++) {
            System.out.println("pos \n" + BitChessBoard.toStr(1L << i));
            System.out.println("South one: \n" + BitChessBoard.toStr(BB.soutOne(1L << i)));
        }
    }


    @Test
    public void testSouth(){
       long pos= 1L << 62;

        System.out.println("pos \n" + BitChessBoard.toStr(pos));
        System.out.println("South one: \n" + BitChessBoard.toStr(BB.noWeOne(pos)));
    }
}