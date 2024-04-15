package org.mattlang.jc.problemanalyzing;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

/**
 * We may have a problem with parsing moves via fen strings for promotions which are captures.
 * Currently we do "lose" the capture in the promotion move during the parsing.
 * However, currently it does not seem to produce errors, since the move doing on the board does the right
 * in the current board implementations (even the bitboard). However - it is a bit dangerous - when we optimize or
 * change the board implementation it could produce failures, leaving the captures piece on the board....
 *
 * todo currently not fixed....
 */
public class ParsingPromotionsWithCapturesFailureTest {

    int maxDepth = 6;

    @Test
    public void queen_and_king_vs_king() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.timeout.setValue(60000)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen 3qk3/2P5/1Q6/4K3/8/8/8/8 w - - 0 0 moves c7d8q ");
        System.out.println(engine.getBoard().toUniCodeStr());

        Move move = engine.go(gameState, new GameContext());

        System.out.println(move.toStr());


    }

}
