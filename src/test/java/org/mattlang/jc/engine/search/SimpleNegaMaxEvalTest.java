package org.mattlang.jc.engine.search;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.engine.evaluation.SimpleBoardStatsGenerator;
import org.mattlang.jc.engine.evaluation.SimpleNegaMaxEval;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.uci.FenParser;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleNegaMaxEvalTest {

    @Test
    public void testChess() {

        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();
        Board board = new Board();

        FenParser parser = new FenParser();
        parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(eval.eval(board, Color.BLACK));
    }


    @Test
    public void testPatt() {
        Board board = new Board();
        board.setFenPosition("position fen 7k/8/6Q1/8/3K4/8/8/8 b k - 2 17 ");

        System.out.println(board.toUniCodeStr());

        SimpleBoardStatsGenerator sbsg = new SimpleBoardStatsGenerator();
        BoardStats stats = sbsg.gen(board, Color.BLACK);

        MaterialNegaMaxEval eval = new MaterialNegaMaxEval();
        int score = eval.eval(board, Color.WHITE);
        System.out.println(score);
    }


    @Test
    public void analyzePattProblem() {
        BoardRepresentation board = Factory.getDefaults().boards.create();
        GameState gameState = board.setFenPosition("position fen 8/8/8/4K3/8/6k1/8/3q4 w - - 14 1 moves e5f5 d1d5 f5f6 g3f2 f6e7 d5e5 e7d7 e5f6 d7c7 f6e6 c7b7 e6d6 b7a7 d6c6 a7b8 c6d7 b8a8");

        System.out.println(board.toUniCodeStr());

        Move move =Factory.getDefaults().searchMethod.create().search(gameState, 6);
        // since we recognize patts, we avoid moves which make patt:
        board.move(move);
        // means we should have no patt situation:
        LegalMoveGenerator moveGenerator = new LegalMoveGeneratorImpl3();
        MoveList whiteMoves = moveGenerator.generate(board, Color.WHITE);
        // so white should have possibilities to move:
        assertThat(whiteMoves.size()).isGreaterThan(0);
    }
}