package org.mattlang.jc.movegenerator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.BoardPrinter;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.tools.LegalMoves;

public class LegalMoveGeneratorImplTest {

    @Test
    public void testSituation1() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 3r4/6n1/8/4K3/8/8/5q2/4k3 w k - 2 17 ");

        System.out.println(BoardPrinter.toUniCodeStr(board));

        MoveList rslt = LegalMoves.generateLegalMoves(board, Color.WHITE);
        /**
         * White king can only move down, because other moves would go into chess
         */

        assertThat(rslt.size()).isEqualTo(1);
        assertThat(getFirstMove(rslt).toStr()).isEqualTo("e5e4");

        // try the same with scnd impl:
        rslt = LegalMoves.generateLegalMoves(board, Color.WHITE);

        assertThat(rslt.size()).isEqualTo(1);
        assertThat(getFirstMove(rslt).toStr()).isEqualTo("e5e4");

    }

    private MoveImpl getFirstMove(MoveList moveList) {
        MoveCursor cursor = LegalMoves.createCursor(moveList);
        cursor.next();
        return new MoveImpl(cursor.getMoveInt());
    }
}