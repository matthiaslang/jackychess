package org.mattlang.jc.tools;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.util.FenComposer;

public class LegalMovesTest {

    @Test
    public void testPromotionProblem(){
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 3k4/6P1/4K3/8/8/8/8/8 b - - 0 52 ");
        String fenBefore=FenComposer.buildFenPosition(board);
        board.println();

        MoveList movelist = LegalMoves.generateLegalMoves(board, Color.WHITE);
        List<MoveImpl> moves = movelist.extractList();
        for (MoveImpl move : moves) {
            System.out.println(move.toStr());
        }
        String fenAfter=FenComposer.buildFenPosition(board);
        Assertions.assertThat(fenAfter).isEqualTo(fenBefore);

        board.println();

    }
}