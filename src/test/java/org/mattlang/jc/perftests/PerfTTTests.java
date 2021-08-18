package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.engine.tt.TTBucketCache;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTDoubleHashingCache;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl3;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfTTTests {

    @Test
    public void ttTest() {
        Board3 board = new Board3();
        board.setStartPosition();
        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());
        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        perftReset();

        TTCache ttCache = new TTCache();
        perft(generator, board, WHITE, 5, (visitedBoard, color, d) -> {
            ttCache.getTTEntry(visitedBoard, color);
            ttCache.storeTTEntry(visitedBoard, color, 0, 0, 0, d, 0);

        });

        Map map = new LinkedHashMap();
        ttCache.collectStatistics(map);
        System.out.println(map);
        // ~30 pct collission
    }

    @Test
    public void ttDoubleHashTest() {
        Board3 board = new Board3();
        board.setStartPosition();
        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());
        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        perftReset();

        TTDoubleHashingCache ttCache = new TTDoubleHashingCache();
        perft(generator, board, WHITE, 5, (visitedBoard, color, d) -> {
            ttCache.getTTEntry(visitedBoard, color);
            ttCache.storeTTEntry(visitedBoard, color, 0, 0, 0, d, 0);

        });

        Map map = new LinkedHashMap();
        ttCache.collectStatistics(map);
        System.out.println(map);
        // ~30 pct collission
    }

    @Test
    public void ttBucketTest() {
        Board3 board = new Board3();
        board.setStartPosition();
        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());
        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        perftReset();

        TTBucketCache ttCache = new TTBucketCache();
        perft(generator, board, WHITE, 5, (visitedBoard, color, d) -> {
            ttCache.getTTEntry(visitedBoard, color);
            ttCache.storeTTEntry(visitedBoard, color, 0, 0, 0, d, 0);

        });

        Map map = new LinkedHashMap();
        ttCache.collectStatistics(map);
        System.out.println(map);
        // ~30 pct collission
    }
}
