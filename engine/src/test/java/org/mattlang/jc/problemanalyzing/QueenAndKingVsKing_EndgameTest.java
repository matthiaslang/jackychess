package org.mattlang.jc.problemanalyzing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.play.EndStatus;
import org.mattlang.jc.play.GameStatusResult;
import org.mattlang.jc.play.Playing;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class QueenAndKingVsKing_EndgameTest {

    int maxDepth = 40;

    @Test
    public void queen_and_king_vs_king() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.maxQuiescence.setValue(50))
                .config(c -> c.useTTCache.setValue(false))
                //                .config(c -> c.evluateFunctions.setValue(MINIMAL_PST))
                .config(c -> c.timeout.setValue(2000)));
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

    @Test
    public void queen_and_king_vs_kingAnalyze() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.timeout.setValue(600)));
        // now starting engine:

        Playing playing = new Playing("position fen 8/8/8/1Q6/8/8/8/K5k1 w - - 0 60 ");

        playing.playGameTillEnd();

        Factory.setDefaults(Factory.createDefaultParameter());
    }

    @Test
    public void queen_and_king_vs_kingGivesRemis() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.timeout.setValue(2000)));
        // now starting engine:

        //        Playing playing = new Playing("position fen k7/8/8/8/2Q5/8/8/5K2 w - - 0 60  ");
        Playing playing = new Playing("position fen k7/8/8/8/2Q5/8/8/5K2 w - - 0 60  ");

        GameStatusResult status = playing.playGameTillEnd();

        Factory.setDefaults(Factory.createDefaultParameter());

        assertThat(status.isEnd()).isTrue();
        assertThat(status.getEndStatus()).isEqualTo(EndStatus.MATT);

    }

}
