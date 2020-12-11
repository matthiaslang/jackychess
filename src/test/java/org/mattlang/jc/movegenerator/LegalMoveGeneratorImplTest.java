package org.mattlang.jc.movegenerator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.BoardPrinter;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

public class LegalMoveGeneratorImplTest {

    @Test
    public void testSituation1() {

        Board board = new Board();
        board.setFenPosition("position fen 3r4/6n1/8/4K3/8/8/5q2/4k3 b k - 2 17 ");

        System.out.println(BoardPrinter.toUniCodeStr(board));

        LegalMoveGenerator legalMoveGenerator = new LegalMoveGeneratorImpl();
        MoveList rslt = legalMoveGenerator.generate(board, Color.WHITE);
        /**
         * White king can only move down, because other moves would go into chess
         */

        assertThat(rslt.size()).isEqualTo(1);
        assertThat(rslt.iterator().next().getMove().toStr()).isEqualTo("e5e4");

        // try the same with scnd impl:
        legalMoveGenerator = new LegalMoveGeneratorImpl2();
        rslt = legalMoveGenerator.generate(board, Color.WHITE);

        assertThat(rslt.size()).isEqualTo(1);
        assertThat(rslt.iterator().next().getMove().toStr()).isEqualTo("e5e4");

    }
}