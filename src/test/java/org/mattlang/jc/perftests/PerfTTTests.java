package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTCacheInterface;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.movegenerator.BBLegalMoveGeneratorImpl;
import org.mattlang.jc.movegenerator.MoveGenerator;
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

    public void testCache(TTCacheInterface ttCache, int depth) {
        BitBoard board = new BitBoard();
        board.setStartPosition();
        //        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

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

    public void testCacheWithPosition(BitBoard board, TTCacheInterface ttCache, int depth) {
        MoveGenerator generator = new BBLegalMoveGeneratorImpl();
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
        TTResult entry = new TTResult();
        perft(generator, board, WHITE, depth, (visitedBoard, color, d) -> {
            if (ttCache.findEntry(entry, visitedBoard)) {
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
