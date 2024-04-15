package org.mattlang.jc.engine.tt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;

public class TTCacheTest {

    @Test
    public void test() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        cache.storeTTEntry(board, Color.WHITE, 500, 300, 900, 7, 0);

        TTResult entry=new TTResult();
        assertThat(cache.findEntry(entry, board)).isTrue();
        assertThat(entry).isNotNull();

        assertThat(entry.getDepth()).isEqualTo(7);
        assertThat(entry.isExact()).isTrue();

    }

    @Test
    public void test2() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        cache.addValue(board.getZobristHash(), -500, 5, TTResult.EXACT_VALUE, 400000);

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

        cache.addValue(board.getZobristHash(), -500, 5, TTResult.EXACT_VALUE, 0);

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
    public void testIntCaches() {

        IntIntCache intIntCache=new IntIntCache(20);


        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        intIntCache.save(board.getZobristHash(), -500);

        board.switchSiteToMove();
        assertThat(intIntCache.find(board.getZobristHash())).isEqualTo(IntIntCache.NORESULT);
        board.switchSiteToMove();

        int entry = intIntCache.find(board.getZobristHash());
        assertThat(entry).isEqualTo(-500);

    }
    @Test
    public void testSizeCalc() {
        assertThat(TTCache.determineCacheBitSizeFromMb(128, 16)).isEqualTo(23);

        assertThat(TTCache.determineCacheBitSizeFromMb(1, 16)).isEqualTo(16);

        assertThat(TTCache.determineCacheBitSizeFromMb(256, 16)).isEqualTo(24);

        // take smaller or higer bit??
        assertThat(TTCache.determineCacheBitSizeFromMb(192, 16)).isEqualTo(23);
    }

}