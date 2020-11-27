package org.mattlang.jc.engine.search;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.SimpleNegaMaxEval;
import org.mattlang.jc.uci.FenParser;

import junit.framework.TestCase;

public class SimpleNegaMaxEvalTest extends TestCase {

    @Test
    public void testChess() {

        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();
        Board board = new Board();

        FenParser parser = new FenParser();
        parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(eval.eval(board, Color.BLACK));
    }

}