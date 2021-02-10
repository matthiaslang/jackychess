package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;

public class PawnStructureEvalTest {

    @Test
    public void singleWhitePassedBorderPawn() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // passed pawn, isolated on border:
        board.setPos(1, 0, Figure.W_Pawn.figureChar);
        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, but penalty of 20 for isolation (border pawn), so
        // we get 6*20 - 20 = 100
        assertThat(pse.eval(board, null, null, WHITE)).isEqualTo(100);
        assertThat(pse.eval(board, null, null, BLACK)).isEqualTo(-100);
    }

    @Test
    public void twoWhitePassedPawn() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(1, 3, Figure.W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(1, 4, Figure.W_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, but penalty of 20 for isolation (border pawn), so
        // we get 6*20 - 20 = 100
        assertThat(pse.eval(board, null, null, WHITE)).isEqualTo(240);
        assertThat(pse.eval(board, null, null, BLACK)).isEqualTo(-240);
    }

    @Test
    public void singlePassedPawnNotIsolated() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(1, 3, Figure.W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(6, 2, Figure.W_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, the other is passed on initial row (+20), but backwarded - 8.
        // so we get 6*20 + 20 - 8 = 132
        assertThat(pse.eval(board, null, null, WHITE)).isEqualTo(132);
        assertThat(pse.eval(board, null, null, BLACK)).isEqualTo(-132);
    }

    @Test
    public void valuesonStartpos() {
        Board3 board = new Board3();
        board.setStartPosition();
        PawnStructureEval pse = new PawnStructureEval();

        System.out.println(board.toUniCodeStr());

        PawnStructureEval.PawnLineChecker
                wChecker = new PawnStructureEval.PawnLineChecker(board.getWhitePieces().getPawns(), Color.WHITE);
        PawnStructureEval.PawnLineChecker
                bChecker = new PawnStructureEval.PawnLineChecker(board.getBlackPieces().getPawns(), Color.BLACK);

        // than we should have no advantage nor penalty at all (no passed ones, all no backwards, no isolated):
        assertThat(pse.analyzeWhite(wChecker, bChecker)).isEqualTo(0);
        assertThat(pse.analyzeBlack(wChecker, bChecker)).isEqualTo(0);

    }

}