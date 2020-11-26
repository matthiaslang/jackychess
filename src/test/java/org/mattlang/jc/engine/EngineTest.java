package org.mattlang.jc.engine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.NegaMax;
import org.mattlang.jc.engine.search.SimpleNegaMaxEval;
import org.mattlang.jc.uci.FenParser;

import junit.framework.TestCase;

public class EngineTest extends TestCase {

    @Test
    public void testNegMax() {

        // now starting engine:
        Engine engine = new Engine(new NegaMax(new SimpleNegaMaxEval()));
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());
    }

    @Test
    public void testNegMax2() {

        // now starting engine:
        Engine engine = new Engine(new NegaMax(new SimpleNegaMaxEval()));
        FenParser parser = new FenParser();
        parser.setPosition("position startpos moves e2e4 a7a6 f2f4 a6a5 a2a4", engine.getBoard());
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());
    }

    /**
     * "Check" is not defined as "invalid" move position, instead we have a weight function which evals a captured
     * king as a "very bad" value, therefore minimax should always get us out of of a chess situation.
     */
    @Test
    public void testCheckSituation() {

        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();

        Board board = new Board();
        FenParser parser = new FenParser();
        parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        NegaMax negaMax = new NegaMax(eval);
        Move move = negaMax.search(board, 2, Color.BLACK);

        // block chess with Rook:
        Assertions.assertThat(move.toStr()).isEqualTo("a7d7");

        System.out.println(move);

    }

}