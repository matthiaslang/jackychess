package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.mattlang.attic.board.Board3;
import org.mattlang.attic.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.attic.movegenerator.MoveGeneratorImpl3;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.tt.*;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.UCI;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfTTTests {

    @Test
    public void ttTest() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        
        testCache(new TTCache(), 5);
    }

    @Test
    public void ttTest2() throws IOException {
        initLogging();
        UCI.instance.attachStreams();

        testCache(new TTCache2(), 5);
    }

    @Test
    public void ttDoubleHashTest() throws IOException {
        initLogging();
        UCI.instance.attachStreams();

        testCache(new TTDoubleHashingCache(), 5);
    }

    @Test
    public void ttBucketTest() throws IOException {
        initLogging();
        UCI.instance.attachStreams();

        testCache(new TTBucketCache(),5);
    }

    public void testCache(TTCacheInterface ttCache, int depth) {
        Board3 board = new Board3();
        board.setStartPosition();
        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        testCacheWithPosition(board, ttCache, depth);

        // do one opening move (knigth) which should not change the tt aging:
        board.domove(new MoveImpl("b1c3"));

        ttCache.updateAging(board);
        testCacheWithPosition(board, ttCache, depth);

        // now a completely different middle game position : bk 1 position:
        GameState gameState = board.setFenPosition("position fen 1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - -  0 0");

        ttCache.updateAging(board);
        testCacheWithPosition(board, ttCache, depth);
    }

    public void testCacheWithPosition(Board3 board, TTCacheInterface ttCache, int depth) {
        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();
        StopWatch fillWatch = new StopWatch();
        perftReset();

        int[] count = new int[1];
        int[] hits = new int[1];
        // fill cache
        fillWatch.start();
        perft(generator, board, WHITE, depth, (visitedBoard, color, d) -> {
            ttCache.storeTTEntry(visitedBoard, color, 0, 0, 0, d, 0);
            count[0]++;
        });
        fillWatch.stop();

        // read from cache:
        StopWatch readWatch = new StopWatch();
        readWatch.start();
        perft(generator, board, WHITE, depth, (visitedBoard, color, d) -> {
            TTEntry foundEntry = ttCache.getTTEntry(visitedBoard, color);
            if (foundEntry != null) {
                hits[0]++;
            }

        });
        readWatch.stop();

        System.out.println("counts " + count[0] + " hits " + hits[0] + "hits: %" + (((long) hits[0]) * 100 / count[0]));
        System.out.println("fill time" + fillWatch.toString());
        System.out.println("read time" + readWatch.toString());

        Map map = new LinkedHashMap();
        ttCache.collectStatistics(map);
        System.out.println(map);
        // ~30 pct collission
    }
}
