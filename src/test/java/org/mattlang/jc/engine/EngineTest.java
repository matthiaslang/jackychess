package org.mattlang.jc.engine;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.NegaMax;
import org.mattlang.jc.engine.search.SimpleNegaMaxEval;

import junit.framework.TestCase;

public class EngineTest extends TestCase {
    @Test
    public void testNegMax() {
        Board board = new Board();
        board.setStartPosition();
        System.out.println(board.toStr());
        
        // now starting engine:
        Engine engine = new Engine(new NegaMax(new SimpleNegaMaxEval()));
        engine.setStartPosition();
        Move move = engine.go();

        System.out.println(move.toStr());
    }
}