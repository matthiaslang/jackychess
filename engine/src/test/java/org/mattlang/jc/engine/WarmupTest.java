package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.SearchParameter.params;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.board.Move;

public class WarmupTest {

    @Test
    @Disabled
    public void testIterativeDeepening() throws IOException {

        TestTools.initUciEngineTest();

        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        SearchParameter params = params(6000000, 63);
        Move move = engine.go(params);

        System.out.println(move.toStr());

        // with the evaluation function it should yield:
        assertThat(move.toStr()).isIn("g1f3", "e2e4");
    }

}