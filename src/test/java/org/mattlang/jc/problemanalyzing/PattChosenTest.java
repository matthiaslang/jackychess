package org.mattlang.jc.problemanalyzing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.uci.UCI;

public class PattChosenTest {

    /**
     * Here a patt has been chosen in a situation where white is in advantage and right before winning.
     * Reason was that the patt has not been recognized, because pawn captures where not taken into count.
     * They where not taken into account, because in patt analyze, we collect "mobility" (possible moves)
     * and treat them as "possible captures", but here the pawn captures where missing, because they differ
     * to pawn moves.
     * To get this to work, we have now the ability to collect hypothetical pawn captures in board statistics.
     *
     * @throws IOException
     */
    @Test
    public void pattChosen() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
        .config(c->c.maxDepth.setValue(2)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen 8/1P1k1p2/5P2/3KP3/2p2B2/2P5/7P/8 w - - 1 56 ");
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(gameState);

        System.out.println(move.toStr());

        engine.getBoard().move(move);
        // means we should have no patt situation:
        LegalMoveGenerator moveGenerator = Factory.getDefaults().legalMoveGenerator.create();
        MoveList whiteMoves = moveGenerator.generate(engine.getBoard(), Color.BLACK);
        // so black should have possibilities to move:
        assertThat(whiteMoves.size()).isGreaterThan(0);


        Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta());
    }

}
