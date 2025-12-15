package org.mattlang.jc.problemanalyzing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class DoesnotCaptureQueenTest {

    @Test
    public void analyzeProblemWhyNotCaptureTheQueen() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();

        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = GameState.posFrom("position fen 8/5k2/8/8/3q4/4K3/8/8 w - - 1 56 ");
        System.out.println(gameState.getBoard().toUniCodeStr());
        Move move = engine.go(new SearchParameter(), gameState);

        System.out.println(move.toStr());
        assertThat(move.toStr()).isEqualTo("e3d4");

    }

}
