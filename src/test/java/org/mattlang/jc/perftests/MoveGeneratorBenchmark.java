package org.mattlang.jc.perftests;

import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl3;
import org.mattlang.jc.uci.UCI;

/**
 * Benchmark move generator. legal move generator takes ~two times of a non legal move generator.
 */
public class MoveGeneratorBenchmark {

    /**
     * Compares speed between "default" alpha beta deepening and the variant with TT cache and zobrist hashing.
     * the opt variatn is slightly faster on depth > 7
     *
     * @throws IOException
     */
    @Test
    public void compareMoveGen() throws IOException {

        initLogging();
        UCI.instance.attachStreams();

        StopWatch measureNonLegal = benchmark(
                "non legal move gen",
                () -> {
                    Engine engine = new Engine();

                    engine.getBoard().setStartPosition();

                    Perft.perft(new MoveGeneratorImpl3(), engine.getBoard(), Color.WHITE, 5, visitedBoard -> {
                    });
                });

        Map itTT = Factory.getDefaults().collectStatistics();

        StopWatch measureLegalMove = benchmark(
                "legal move gen",
                () -> {
                    Engine engine = new Engine();

                    engine.getBoard().setStartPosition();
                    Perft.perft(new LegalMoveGeneratorImpl3(), engine.getBoard(), Color.WHITE, 5, visitedBoard -> {
                    });
                });
        Map itNormal = Factory.getDefaults().collectStatistics();

        System.out.println("legal move gen: " + measureLegalMove.toString());
        System.out.println("non legal move gen: " + measureNonLegal.toString());

    }

}
