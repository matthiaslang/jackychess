package org.mattlang.jc.perftests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.mattlang.attic.board.Board3;
import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

/**
 * PerfTests for zobrist hashing.
 */
public class ZobristPerfTests2 {

    public static final int MAX_DEPTH = 6;
    public static final int TIMEOUT = 60000;
    public static final String POSITION =
            "position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w - - 0 0";

    /**
     * Compares speed between "default" alpha beta deepening and the variant with TT cache and zobrist hashing.
     * the opt variatn is slightly faster on depth > 7
     *
     * @throws IOException
     */
    @Test
    public void compareSpeed() throws IOException {

        initLogging();
        UCI.instance.attachStreams();

        StopWatch ttMeasure = benchmark(
                "iterative deepening alpha beta TT zobrist",
                () -> {
                    Factory.setDefaults(Factory.createIterativeDeepeningPVS()
                            .boards.set(() -> new Board3())
                            .config(c -> c.timeout.setValue(TIMEOUT))
                            .config(c -> c.activatePvsSearch.setValue(true))
                            .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                            .config(c -> c.useTTCache.setValue(true)));

                    // now starting engine:
                    Engine engine = new Engine();
                    GameState state = engine.getBoard()
                            .setFenPosition(POSITION);
                    System.out.println(engine.getBoard().toUniCodeStr());
                    Move move = engine.go(state, new GameContext());
                });

        Map itTT = Factory.getDefaults().collectStatistics();

        StopWatch normalMeasure = benchmark(
                "iterative deepening alpha beta",
                () -> {
                    Factory.setDefaults(Factory.createIterativeDeepeningPVS()
                            .config(c -> c.timeout.setValue(TIMEOUT))
                            .config(c -> c.activatePvsSearch.setValue(true))
                            .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                            .config(c -> c.useTTCache.setValue(false))
                            .boards.set(() -> new Board3()));

                    // now starting engine:
                    Engine engine = new Engine();
                    GameState state =
                            engine.getBoard()
                                    .setFenPosition(POSITION);
                    System.out.println(engine.getBoard().toUniCodeStr());
                    Move move = engine.go(state, new GameContext());
                });
        Map itNormal = Factory.getDefaults().collectStatistics();

        System.out.println("normal time: " + normalMeasure.toString());
        System.out.println("tt zobrist time: " + ttMeasure.toString());

        SearchParameter.printStats("tt zobrist", itTT);
        SearchParameter.printStats("normal", itNormal);

        assertThat(ttMeasure.getDuration()).isLessThan(normalMeasure.getDuration());

    }

}
