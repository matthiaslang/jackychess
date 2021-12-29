package org.mattlang.jc.engine.tt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.attic.board.Board3;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class TTCacheTest {

    @Test
    public void test() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new Board3();
        board.setStartPosition();

        cache.storeTTEntry(board, Color.WHITE, 500, 300, 900, 7, 0);

        TTEntry entry = cache.getTTEntry(board, Color.WHITE);
        assertThat(entry).isNotNull();

        assertThat(entry.getDepth()).isEqualTo(7);
        assertThat(entry.isExact()).isTrue();

    }

    @Test
    public void test2() {
        TTCache3 cache = new TTCache3();

        BoardRepresentation board = new Board3();
        board.setStartPosition();

        cache.addValue(board.getZobristHash(), 500, 5, TTEntry.EXACT_VALUE, 400000);

        board.switchSiteToMove();
        assertThat(cache.getValue(board.getZobristHash())).isEqualTo(0L);
        board.switchSiteToMove();

        long entry = cache.getValue(board.getZobristHash());

        assertThat(entry).isNotEqualTo(0L);

        assertThat(cache.getDepth(entry)).isEqualTo(5);
        assertThat(TTCache3.getFlag(entry)).isEqualTo(TTEntry.EXACT_VALUE);
        assertThat(TTCache3.getScore(entry)).isEqualTo(500);
        assertThat(TTCache3.getMove(entry)).isEqualTo(400000);

    }

    @Test
    public void test3() {
        TTCache3 cache = new TTCache3();

        BoardRepresentation board = new Board3();
        board.setStartPosition();

        cache.addValue(board.getZobristHash(), -500, -5, TTEntry.EXACT_VALUE, 0);

        board.switchSiteToMove();
        assertThat(cache.getValue(board.getZobristHash())).isEqualTo(0L);
        board.switchSiteToMove();

        long entry = cache.getValue(board.getZobristHash());

        assertThat(entry).isNotEqualTo(0L);

        assertThat(cache.getDepth(entry)).isEqualTo(-5);
        assertThat(TTCache3.getFlag(entry)).isEqualTo(TTEntry.EXACT_VALUE);
        assertThat(TTCache3.getScore(entry)).isEqualTo(-500);

    }
}