package org.mattlang.jc.board;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;

public class CastlingRightsTest {

    @Test
    public void testCastlingRights() {
        CastlingRights rights = new CastlingRights();

        assertThat(rights.isAllowed(WHITE, SHORT)).isTrue();
        assertThat(rights.isAllowed(BLACK, SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE, LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK, LONG)).isTrue();

        rights.retain(WHITE, SHORT);
        assertThat(rights.isAllowed(WHITE, SHORT)).isFalse();
        assertThat(rights.isAllowed(BLACK, SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE, LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK, LONG)).isTrue();

        rights.retain(BLACK, LONG);
        assertThat(rights.isAllowed(WHITE, SHORT)).isFalse();
        assertThat(rights.isAllowed(BLACK, SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE, LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK, LONG)).isFalse();

        rights.setAllowed(BLACK, LONG);
        assertThat(rights.isAllowed(WHITE, SHORT)).isFalse();
        assertThat(rights.isAllowed(BLACK, SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE, LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK, LONG)).isTrue();
    }
}