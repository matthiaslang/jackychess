package org.mattlang.jc.perftests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Benchmarks.benchmark;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.CachingEvaluateFunction;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt;
import org.mattlang.jc.engine.search.IterativeDeepeningNegaMaxAlphaBeta;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.movegenerator.CachingLegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.ZobristBoardCache;

/**
 * PerfTests for zobrist hashing.
 */
public class ZobristPerfTests2 {

    /**
     * Compares speed between "default" alpha beta deepening and the variant with TT cache and zobrist hashing.
     * the opt variatn is slightly faster on depth > 7
     * @throws IOException
     */
    @Test
    public void compareSpeed() throws IOException {

//        initLogging();
//        UCI.instance.attachStreams();

        StopWatch hashMeasure = benchmark(
                "iterative deepening alpha beta",
                () -> {
                    Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
                            .config(c->c.timeout.setValue(60000))
                            .config(c->c.maxDepth.setValue(7))
                            .evaluateFunction.set(() -> new CachingEvaluateFunction(new MaterialNegaMaxEvalOpt()))
                            .legalMoveGenerator.set(() -> new CachingLegalMoveGenerator(new LegalMoveGeneratorImpl3()))
                    );
                    // now starting engine:
                    Engine engine = new Engine();
                    GameState state= engine.getBoard().setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w - - 0 0");
                    System.out.println(engine.getBoard().toUniCodeStr());
                    Move move = engine.go(state);
                                   });

        Map itDeepStats = Factory.getDefaults().collectStatistics();
        
        StopWatch zobristMeasure = benchmark(
                "iterative deepening alpha beta TT with zobrist",
                () -> {
                    Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
                            .config(c -> c.timeout.setValue(60000))
                            .config(c -> c.maxDepth.setValue(7))
                            .boards.set(() -> new Board3())
                            .searchMethod.set(() -> new IterativeDeepeningNegaMaxAlphaBeta(
                                    new NegaMaxAlphaBetaPVS().setDoCaching(true)))
                            .evaluateFunction.set(() -> new CachingEvaluateFunction(new ZobristBoardCache<>(), new MaterialNegaMaxEvalOpt()))
                            .legalMoveGenerator.set(() -> new CachingLegalMoveGenerator(new ZobristBoardCache<>(), new LegalMoveGeneratorImpl3()))
                    );
                    // now starting engine:
                    Engine engine = new Engine();
                    GameState state =
                            engine.getBoard().setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w - - 0 0");
                    System.out.println(engine.getBoard().toUniCodeStr());
                    Move move = engine.go(state);
                });
        Map itDeepZobrStats = Factory.getDefaults().collectStatistics();

        System.out.println("zobrist time: " + zobristMeasure.toString());
        System.out.println("hash    time: " + hashMeasure.toString());

        SearchParameter.printStats("default", itDeepStats);
        SearchParameter.printStats("zobrist", itDeepZobrStats);

        assertThat(zobristMeasure.getDuration()).isLessThan(hashMeasure.getDuration());

    }

}
