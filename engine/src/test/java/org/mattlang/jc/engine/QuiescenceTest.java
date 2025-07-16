package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class QuiescenceTest {

    @Test
    public void testQuiescenceExample() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();

        ConfigValues.getConfigValues().maxQuiescence.setValue(5);

        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen K7/8/8/2Q5/2r5/3b4/8/7k w - - 1 56 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        SearchParameter params = SearchParameter.params(60000, 2);
        Move move = engine.go(params, gameState, new GameContext());

        ConfigValues.resetConfigValues();

        assertThat(move.toStr()).isNotEqualTo("c5c4");

        System.out.println(move.toStr());

    }
}
