package org.mattlang.jc;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

import java.io.IOException;

import static org.mattlang.jc.SearchParameter.params;

public class EngineTest {

    @Test
    public void testIterativeDeepening() throws IOException {

        TestTools.initUciEngineTest();

        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = GameState.startPos();
        System.out.println(gameState.getBoard().toUniCodeStr());
        SearchParameter parameter = params(60000, 9);
        Move move = engine.go(parameter, gameState);

        System.out.println(move.toStr());

        // with the evaluation function it should yield:
//        assertThat(move.toStr()).isIn("g1f3", "e2e4");
    }

}