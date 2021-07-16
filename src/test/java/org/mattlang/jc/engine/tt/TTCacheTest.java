package org.mattlang.jc.engine.tt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class TTCacheTest {

    @Test
    public void test() {
        TTCache cache = new TTCache();

        BoardRepresentation board = new Board3();
        board.setStartPosition();

        cache.storeTTEntry(board, Color.WHITE, 500, 300, 900, 7);

        TTEntry entry = cache.getTTEntry(board, Color.WHITE);
        assertThat(entry).isNotNull();

        assertThat(entry.getDepth()).isEqualTo(7);
        assertThat(entry.isExact()).isTrue();
    }
}