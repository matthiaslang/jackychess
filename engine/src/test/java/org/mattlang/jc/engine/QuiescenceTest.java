package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class QuiescenceTest {


    @Test
    public void testQuiescenceExample() throws IOException {

        initLogging();
        UCI.instance.attachStreams();

        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c->c.maxDepth.setValue(2))
                .config(c->c.maxQuiescence.setValue(5))
        );
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen K7/8/8/2Q5/2r5/3b4/8/7k w - - 1 56 ");
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(gameState, new GameContext());

        assertThat(move.toStr()).isNotEqualTo("c5c4");

        System.out.println(move.toStr());

        Factory.getDefaults().printStats();
    }
}
