package org.mattlang.jc.problemanalyzing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class DoesnotCaptureQueenTest {


    @Test
    public void analyzeProblemWhyNotCaptureTheQueen() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
        .config(c->c.maxDepth.setValue(7)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen 8/5k2/8/8/3q4/4K3/8/8 w - - 1 56 ");
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(gameState, new GameContext());

        System.out.println(move.toStr());
         assertThat(move.toStr()).isEqualTo("e3d4");



        Factory.setDefaults(Factory.createDefaultParameter());
    }

}
