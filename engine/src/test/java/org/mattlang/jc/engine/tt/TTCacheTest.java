package org.mattlang.jc.engine.tt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;

public class TTCacheTest {

    @Test
    public void test() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        cache.storeTTEntry(board, -500, -900, -300, 7, 345, -33);

        TTResult entry = new TTResult();
        assertThat(cache.findEntry(entry, board)).isTrue();
        assertThat(entry).isNotNull();

        assertThat(entry.getEval()).isEqualTo(-33);
        assertThat(entry.getDepth()).isEqualTo(7);
        assertThat(entry.getScore()).isEqualTo(-500);
        assertThat(entry.getMove()).isEqualTo(345);
        assertThat(entry.isExact()).isTrue();

    }

    @Test
    public void test2() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        cache.addValue(board.getZobristHash(), -500, 5, TTResult.EXACT_VALUE, 400000, 0);

        board.switchSiteToMove();
        assertThat(cache.getValue(board.getZobristHash())).isEqualTo(TTCache.NORESULT);
        board.switchSiteToMove();

        long entry = cache.getValue(board.getZobristHash());

        assertThat(entry).isNotEqualTo(TTCache.NORESULT);

        assertThat(cache.getDepth(entry)).isEqualTo(5);
        assertThat(TTCache.getFlag(entry)).isEqualTo(TTResult.EXACT_VALUE);
        assertThat(TTCache.getScore(entry)).isEqualTo(-500);
        assertThat(TTCache.getMove(entry)).isEqualTo(400000);

    }

    @Test
    public void test3() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        cache.addValue(board.getZobristHash(), -500, 5, TTResult.EXACT_VALUE, 0, 0);

        board.switchSiteToMove();
        assertThat(cache.getValue(board.getZobristHash())).isEqualTo(TTCache.NORESULT);
        board.switchSiteToMove();

        long entry = cache.getValue(board.getZobristHash());

        assertThat(entry).isNotEqualTo(TTCache.NORESULT);

        assertThat(cache.getDepth(entry)).isEqualTo(5);
        assertThat(TTCache.getFlag(entry)).isEqualTo(TTResult.EXACT_VALUE);
        assertThat(TTCache.getScore(entry)).isEqualTo(-500);

    }


    @Test
    public void testSizeCalc2() {

        ConfigValues.getConfigValues().hash.setValue(16);

        Caching.CACHING.getTtCache().checkUpdateCacheSize();

        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(16 * 1024 * 1024);
        ConfigValues.getConfigValues().hash.setValue(8);

        Caching.CACHING.getTtCache().checkUpdateCacheSize();
        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(8 * 1024 * 1024);

        ConfigValues.getConfigValues().hash.setValue(9);
        Caching.CACHING.getTtCache().checkUpdateCacheSize();
        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(8 * 1024 * 1024);

        // testing int overflow problem: commented out, to not allocate 2gb during tests
//        ConfigValues.getConfigValues().hash.setValue(3000);
//        Caching.CACHING.getTtCache().checkUpdateCacheSize();
//        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(3000L * 1024 * 1024);

        ConfigValues.resetConfigValues();
    }

}