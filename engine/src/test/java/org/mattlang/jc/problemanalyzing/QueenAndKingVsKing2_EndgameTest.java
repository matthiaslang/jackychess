package org.mattlang.jc.problemanalyzing;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class QueenAndKingVsKing2_EndgameTest {

    int maxDepth = 10;

    @Test
    public void queen_and_king_vs_king() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.useTTCache.setValue(false))
                //                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))

                .config(c -> c.timeout.setValue(6000000)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen 4k3/8/1Q6/4K3/8/8/8/8 w - - 0 0 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        SearchMethod searchMethod = Factory.getDefaults().searchMethod.create();

        IterativeDeepeningPVS itDeep = (IterativeDeepeningPVS) searchMethod;
        IterativeSearchResult itResult =
                itDeep.iterativeSearch(gameState, new GameContext(), maxDepth);

        // execute moves on board of pv:
        for (Move move : itResult.getRslt().pvList.getPvMoves()) {
            Color siteToMove = gameState.getBoard().getSiteToMove();
            gameState.getBoard().domove(move);

            System.out.println(siteToMove + ": " + move.toStr());
            System.out.println(gameState.getBoard().toUniCodeStr());
        }

        Factory.setDefaults(Factory.createDefaultParameter());
    }

}
