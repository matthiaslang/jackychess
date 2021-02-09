package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.Figure;

public class PawnStructureEvalTest {

    @Test
    public void test() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a non passed pawn:
        board.setPos(1, 0, Figure.W_Pawn.figureChar);
        board.setPos(1, 4, Figure.B_Pawn.figureChar);

        // so eval is 0 for both sides:
        assertThat(pse.eval(board, null, null, WHITE)).isEqualTo(0);
        assertThat(pse.eval(board, null, null, BLACK)).isEqualTo(0);

        // add a passed pawn for white:
        board.setPos(2, 5, Figure.W_Pawn.figureChar);

        assertThat(pse.eval(board, null, null, WHITE)).isEqualTo(8);
        assertThat(pse.eval(board, null, null, BLACK)).isEqualTo(-8);

        // double pawn penaltry:
        board.setPos(3, 5, Figure.W_Pawn.figureChar);

        assertThat(pse.eval(board, null, null, WHITE)).isEqualTo(-10);
        assertThat(pse.eval(board, null, null, BLACK)).isEqualTo(+10);

    }
}