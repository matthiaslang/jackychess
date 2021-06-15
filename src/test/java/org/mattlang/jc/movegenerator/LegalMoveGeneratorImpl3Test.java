package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.uci.UCI;

public class LegalMoveGeneratorImpl3Test {


    @Test
    public void testNonQuietMoves() throws IOException {
        initLogging();
        UCI.instance.attachStreams();

        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c->c.maxDepth.setValue(1))
                .config(c->c.maxQuiescence.setValue(5))
        );
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen K7/8/8/8/2Q5/3b4/8/7k w - - 1 56 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        LegalMoveGeneratorImpl3 movegen = new LegalMoveGeneratorImpl3();
        MoveList moves = movegen.generateNonQuietMoves(engine.getBoard(), Color.BLACK);
        System.out.println(moves);
        Assertions.assertThat(moves.size()).isEqualTo(2);
    }
}