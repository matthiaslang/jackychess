package org.mattlang.jc.perftests;

import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.movegenerator.BBLegalMoveGeneratorImpl;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
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

                    Perft.perft(new BBMoveGeneratorImpl(), engine.getBoard(), Color.WHITE, 5, (visitedBoard,c,d) -> {
                    });
                });

        Map itTT = Factory.getDefaults().collectStatistics();
        LegalMoveGenerator legalMoveGen = new BBLegalMoveGeneratorImpl();
        StopWatch measureLegalMove = benchmark(
                "legal move gen",
                () -> {
                    Engine engine = new Engine();

                    engine.getBoard().setStartPosition();

                    Perft.perft(legalMoveGen, engine.getBoard(), Color.WHITE, 5, (visitedBoard,c,d) -> {
                    });
                });
        Map itNormal = Factory.getDefaults().collectStatistics();

        StopWatch measureBBLegalMove = benchmark(
                "bb legal move gen",
                () -> {
                    Engine engine = new Engine();
                    LegalMoveGenerator generator = initBitBoardMoveGen();

                    engine.getBoard().setStartPosition();
                    Perft.perft(generator, engine.getBoard(), Color.WHITE, 5, (visitedBoard,c,d) -> {
                    });
                });

        System.out.println("legal move gen: " + measureLegalMove.toString());
        System.out.println("bb legal move gen: " + measureBBLegalMove.toString());
        System.out.println("non legal move gen: " + measureNonLegal.toString());

    }

    private LegalMoveGenerator initBitBoardMoveGen() {
        Factory.getDefaults().boards.set(() -> new BitBoard());
        Factory.getDefaults().moveGenerator.set(() -> new BBMoveGeneratorImpl());
        LegalMoveGenerator generator = new BBLegalMoveGeneratorImpl();
        return generator;
    }
}
