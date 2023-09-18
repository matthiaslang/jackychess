package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BB.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPawnEvaluation.getPawnNeighbours;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;

public class ParameterizedPawnEvaluationTest {

    @Test
    public void testPawnNeighbors() {

        System.out.println(BB.toStr(BB.notAFile));
        System.out.println(BB.toStr(BB.notHFile));

        BitBoard board = new BitBoard();
        board.setStartPosition();

        long neighbours = getPawnNeighbours(board.getBoard().getPawns(BitChessBoard.nWhite));
        System.out.println(BB.toStr(neighbours));
        Assertions.assertThat(neighbours).isEqualTo(B2 | C2 | D2 | E2 | F2 | G2);

        neighbours = getPawnNeighbours(board.getBoard().getPawns(BitChessBoard.nBlack));
        System.out.println(BB.toStr(neighbours));
        Assertions.assertThat(neighbours).isEqualTo(B7 | C7 | D7 | E7 | F7 | G7);
    }
}