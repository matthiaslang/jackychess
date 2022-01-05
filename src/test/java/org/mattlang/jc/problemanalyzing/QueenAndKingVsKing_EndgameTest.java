package org.mattlang.jc.problemanalyzing;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class QueenAndKingVsKing_EndgameTest {

    int maxDepth = 9;

    @Test
    public void queen_and_king_vs_king() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.maxQuiescence.setValue(10))
                .config(c -> c.useTTCache.setValue(false))
//                .config(c -> c.evluateFunctions.setValue(MINIMAL_PST))
                .config(c -> c.timeout.setValue(600)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen 8/8/4k3/8/3Q4/4K3/8/8 w - - 0 0 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        SearchMethod searchMethod = Factory.getDefaults().searchMethod.create();

        IterativeDeepeningPVS itDeep = (IterativeDeepeningPVS) searchMethod;
        IterativeSearchResult itResult =
                itDeep.iterativeSearch(gameState, new GameContext(), maxDepth);

        // execute moves on board of pv:
        for (Move move : itResult.getRslt().pvList.getPvMoves()) {
            gameState.getBoard().domove(move);

            System.out.println(move.toStr());
            System.out.println(gameState.getBoard().toUniCodeStr());
        }

        Factory.setDefaults(Factory.createDefaultParameter());
    }

}
