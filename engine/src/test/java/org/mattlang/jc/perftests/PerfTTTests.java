package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.IndexConversion.parsePos;
import static org.mattlang.jc.perftests.PerfTests.DEFAULT_SUPPLIER;

import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.perft.Perft;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
@Category(SlowTests.class)
public class PerfTTTests {

    @Test
    public void ttTest() throws IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();

        testCache(new TTCache(), 5);
    }

    public void testCache(TTCache ttCache, int depth) {
        BitBoard board = new BitBoard();
        board.setStartPosition();
        //        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        testCacheWithPosition(board, ttCache, depth);

        // do one opening move (knigth) which should not change the tt aging:
        board.domove(new MoveImpl(FigureConstants.FT_KNIGHT, parsePos("b1"), parsePos("c3"), (byte) 0));

        ttCache.updateAging(board);
        testCacheWithPosition(board, ttCache, depth);

        // now a completely different middle game position : bk 1 position:
        GameState gameState =
                board.setFenPosition("position fen 1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - -  0 0");

        ttCache.updateAging(board);
        testCacheWithPosition(board, ttCache, depth);
    }

    public void testCacheWithPosition(BitBoard board, TTCache ttCache, int depth) {
        StopWatch fillWatch = new StopWatch();
        Perft perft = new Perft(DEFAULT_SUPPLIER);

        int[] count = new int[1];
        int[] hits = new int[1];

        perft.setVisitor((visitedBoard, color, d, cursor) -> {
            ttCache.storeTTEntry(visitedBoard, color, 0, 0, 0, d, 0);
            count[0]++;
        });

        // fill cache
        fillWatch.start();
        perft.perft(board, board.getSiteToMove(), depth);
        fillWatch.stop();

        // read from cache:
        StopWatch readWatch = new StopWatch();
        readWatch.start();

        TTResult entry = new TTResult();
        perft.setVisitor((visitedBoard, color, d, cursor) -> {
            if (ttCache.findEntry(entry, visitedBoard)) {
                hits[0]++;
            }

        });

        perft.perft(board, board.getSiteToMove(), depth);
        readWatch.stop();

        System.out.println("counts " + count[0] + " hits " + hits[0] + "hits: %" + (((long) hits[0]) * 100 / count[0]));
        System.out.println("fill time" + fillWatch.toString());
        System.out.println("read time" + readWatch.toString());

        // ~30 pct collission
    }
}
