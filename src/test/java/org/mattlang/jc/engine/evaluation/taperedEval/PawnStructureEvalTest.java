package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.B_Pawn;
import static org.mattlang.jc.board.Figure.W_Pawn;

import org.junit.Test;
import org.mattlang.jc.board.Board3;

public class PawnStructureEvalTest {

    @Test
    public void singleWhitePassedBorderPawn() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // passed pawn, isolated on border:
        board.setPos(1, 0, W_Pawn.figureChar);
        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, but penalty of 20 for isolation (border pawn), so
        // we get 6*20 - 20 = 100
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(100);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(-100);
    }

    @Test
    public void singleBlackPassedBorderPawn() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // passed pawn, isolated on border:
        board.setPos(6, 0, B_Pawn.figureChar);
        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, but penalty of 20 for isolation (border pawn), so
        // we get 6*20 - 20 = 100
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(-100);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(100);
    }

    @Test
    public void twoWhitePassedPawn() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(1, 3, W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(1, 4, W_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, but penalty of 20 for isolation (border pawn), so
        // we get 6*20 - 20 = 100
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(240);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(-240);
    }

    @Test
    public void twoBlackPassedPawn() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(6, 3, B_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(6, 4, B_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, but penalty of 20 for isolation (border pawn), so
        // we get 6*20 - 20 = 100
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(-240);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(240);
    }

    @Test
    public void singleWhitePassedPawnNotIsolated() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(1, 3, W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(6, 2, W_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, the other is passed on initial row (+20), but backwarded - 8.
        // so we get 6*20 + 20 - 8 = 132
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(132);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(-132);
    }

    @Test
    public void singleBlackPassedPawnNotIsolated() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(6, 3, B_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(1, 2, B_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // passed pawn one row before promotion = 6*20, the other is passed on initial row (+20), but backwarded - 8.
        // so we get 6*20 + 20 - 8 = 132
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(-132);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(132);
    }

    @Test
    public void valuesOnStartpos() {
        Board3 board = new Board3();
        board.setStartPosition();
        PawnStructureEval pse = new PawnStructureEval();

        System.out.println(board.toUniCodeStr());

        PawnRanks wChecker = new PawnRanks(board.getWhitePieces().getPawns(), WHITE);
        PawnRanks bChecker = new PawnRanks(board.getBlackPieces().getPawns(), BLACK);

        // than we should have no advantage nor penalty at all (no passed ones, all no backwards, no isolated):
        assertThat(pse.analyzeWhite(wChecker, bChecker)).isEqualTo(0);
        assertThat(pse.analyzeBlack(wChecker, bChecker)).isEqualTo(0);

    }

    @Test
    public void whiteAndBlackPassedPawnsOnSameLine() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(1, 3, W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(6, 3, B_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // evaluated they are both equal:
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(0);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(0);

        // individual each one has a passed pawn on the rank before promotion (but isolated):
        PawnRanks wChecker = new PawnRanks(board.getWhitePieces().getPawns(), WHITE);
        PawnRanks bChecker = new PawnRanks(board.getBlackPieces().getPawns(), BLACK);

        // so it has 6* 20 (passed pawn) - 20 (isolated)
        assertThat(pse.analyzeWhite(wChecker, bChecker)).isEqualTo(120 - 20);
        // the same for black:
        assertThat(pse.analyzeBlack(wChecker, bChecker)).isEqualTo(120 - 20);
    }

    @Test
    public void whiteAndBlackNoPassedPawnSameLine() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(6, 3, W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(1, 3, B_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // evaluated they are both equal:
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(0);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(0);

        // individual each one has no passed one; and they are isolated:
        PawnRanks wChecker = new PawnRanks(board.getWhitePieces().getPawns(), WHITE);
        PawnRanks bChecker = new PawnRanks(board.getBlackPieces().getPawns(), BLACK);

        // so it has 6* 20 (passed pawn) - 20 (isolated)
        assertThat(pse.analyzeWhite(wChecker, bChecker)).isEqualTo(-20);
        // the same for black:
        assertThat(pse.analyzeBlack(wChecker, bChecker)).isEqualTo(-20);
    }


    @Test
    public void whiteAndBlackNoPassedPawnNeighborLine1() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(6, 4, W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(1, 3, B_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // evaluated they are both equal:
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(0);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(0);

        // individual each one has no passed one; and they are isolated:
        PawnRanks wChecker = new PawnRanks(board.getWhitePieces().getPawns(), WHITE);
        PawnRanks bChecker = new PawnRanks(board.getBlackPieces().getPawns(), BLACK);

        // so it has 6* 20 (passed pawn) - 20 (isolated)
        assertThat(pse.analyzeWhite(wChecker, bChecker)).isEqualTo(-20);
        // the same for black:
        assertThat(pse.analyzeBlack(wChecker, bChecker)).isEqualTo(-20);
    }

    @Test
    public void whiteAndBlackNoPassedPawnNeighborLine2() {
        Board3 board = new Board3();
        PawnStructureEval pse = new PawnStructureEval();

        // a passed pawn:
        board.setPos(6, 3, W_Pawn.figureChar);

        // and another one on its side, so no one is isolated:
        board.setPos(1, 4, B_Pawn.figureChar);

        System.out.println(board.toUniCodeStr());

        // evaluated they are both equal:
        assertThat(pse.eval(new EvalStats(board), WHITE)).isEqualTo(0);
        assertThat(pse.eval(new EvalStats(board), BLACK)).isEqualTo(0);

        // individual each one has no passed one; and they are isolated:
        PawnRanks wChecker = new PawnRanks(board.getWhitePieces().getPawns(), WHITE);
        PawnRanks bChecker = new PawnRanks(board.getBlackPieces().getPawns(), BLACK);

        // so it has 6* 20 (passed pawn) - 20 (isolated)
        assertThat(pse.analyzeWhite(wChecker, bChecker)).isEqualTo(-20);
        // the same for black:
        assertThat(pse.analyzeBlack(wChecker, bChecker)).isEqualTo(-20);
    }
}