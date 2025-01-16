package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.board.Move;

public class WarmupTest {

    @Test
    public void testIterativeDeepening() throws IOException {

        TestTools.initUciEngineTest();

        Factory.setDefaults(Factory.createStable()
                .config(c -> c.timeout.setValue(6000000))
                .config(c -> c.maxDepth.setValue(63)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield:
        assertThat(move.toStr()).isIn("g1f3", "e2e4");
    }

}