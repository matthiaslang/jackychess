package org.mattlang.jc.problemanalyzing;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
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
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position fen 4k3/8/1Q6/4K3/8/8/8/8 w - - 0 0 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        IterativeDeepeningPVS itDeep = new IterativeDeepeningPVS();
        IterativeSearchResult itResult =
                itDeep.iterativeSearch(SearchParameter.params(6000000, maxDepth), gameState, new GameContext());

        // execute moves on board of pv:
        for (Move move : itResult.getRslt().getPvMoves()) {
            Color siteToMove = gameState.getBoard().getSiteToMove();
            gameState.getBoard().domove(move);

            System.out.println(siteToMove + ": " + move.toStr());
            System.out.println(gameState.getBoard().toUniCodeStr());
        }

    }

}
