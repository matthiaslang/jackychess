package org.mattlang.jc.perftests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.SearchParameter.params;

import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.tt.Caching;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.util.Logging;

import lombok.Data;

/**
 * Testcode to analyze tt cache.
 */
@Tag("SlowTests")
public class CachePerformanceTests {

    @Data
    public static class CacheInfo {

        private final int noReplaceCacheAlreadyBetter;
        private final long cacheMisses;
        private final long cacheHits;
        private final int cacheSize;
        private final long usage;
        private final int numFieldEntries;
        private final int numMaxEntries;
        private final long preciseUsage;
        private final int[] heuristic;

        public CacheInfo(TTCache ttCache) {
            cacheSize = ttCache.getCacheSize();
            cacheHits = ttCache.getCacheHits();
            cacheMisses = ttCache.getCacheMisses();
            noReplaceCacheAlreadyBetter = ttCache.getNoReplaceCacheAlreadyBetter();
            usage = ttCache.getUsagePercentage();
            preciseUsage = ttCache.getPreciseUsagePercentage();
            numFieldEntries = ttCache.getNumFilledEntries();
            numMaxEntries = cacheSize / 8 / 2;
            heuristic = ttCache.fillHeuristic();
        }

        public void printInfo() {
            System.out.println("Cache: hits: " + cacheHits + " misses: " + cacheMisses + " noreplace: "
                               + noReplaceCacheAlreadyBetter);
            System.out.println(
                    "Cache usage: " + usage + " precise usage: " + preciseUsage + " numFieldEntries: " + numFieldEntries
                    + " max: " + numMaxEntries + " numField ratio permille: " + (numFieldEntries * 1000
                                                                                 / numMaxEntries));
        }

        public void printHeuristic() {
            ArrayFunction f = new ArrayFunction(heuristic);
            System.out.println(f.convertDataToString());
        }
    }

    @Test
    public void testCacheStatsValues() throws IOException {

        TestTools.initUciEngineTest();
        System.setProperty("jacky.logging.activate", "false");
        Logging.initLogging();
        //        ConfigValues.getConfigValues().hash.setValue(256);
        Caching.CACHING.getTtCache().reset();
        GameContext gamecontext = new GameContext();
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        SearchParameter parameter = params(60000, 12);
        Move move = engine.go(parameter, new GameState(engine.getBoard()), gamecontext);

        System.out.println(move.toStr());

        // with the evaluation function it should yield:
        assertThat(move.toStr()).isIn("d2d4", "g1f3", "e2e4");

        CacheInfo run1 = new CacheInfo(Caching.CACHING.getTtCache());

        Caching.CACHING.getTtCache().resetStatistics();
        // redo same "go":
        parameter = params(60000, 12);
        move = engine.go(parameter, new GameState(engine.getBoard()), gamecontext);

        CacheInfo run2 = new CacheInfo(Caching.CACHING.getTtCache());

        Caching.CACHING.getTtCache().resetStatistics();
        // redo same "go":
        parameter = params(60000, 12);
        move = engine.go(parameter, new GameState(engine.getBoard()), gamecontext);

        CacheInfo run3 = new CacheInfo(Caching.CACHING.getTtCache());

        Caching.CACHING.getTtCache().resetStatistics();
        // redo same "go":
        parameter = params(600000, 15);
        move = engine.go(parameter, new GameState(engine.getBoard()), gamecontext);

        CacheInfo run4 = new CacheInfo(Caching.CACHING.getTtCache());

        System.out.println("run 1 info:");
        run1.printInfo();
        System.out.println("run 2 info:");
        run2.printInfo();
        System.out.println("run 3 info:");
        run3.printInfo();
        System.out.println("run 4 info:");
        run3.printInfo();

        run3.printHeuristic();
    }
}
