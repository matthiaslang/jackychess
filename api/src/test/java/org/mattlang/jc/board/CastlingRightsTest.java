package org.mattlang.jc.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.CastlingType.*;

import org.junit.Test;

public class CastlingRightsTest {

    @Test
    public void testCastlingRights() {
        CastlingRights rights = new CastlingRights();

        assertThat(rights.isAllowed(WHITE_SHORT)).isTrue();
        assertThat(rights.isAllowed(BLACK_SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE_LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK_LONG)).isTrue();

        rights.removeRight(WHITE_SHORT);
        assertThat(rights.isAllowed(WHITE_SHORT)).isFalse();
        assertThat(rights.isAllowed(BLACK_SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE_LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK_LONG)).isTrue();

        rights.removeRight(BLACK_LONG);
        assertThat(rights.isAllowed(WHITE_SHORT)).isFalse();
        assertThat(rights.isAllowed(BLACK_SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE_LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK_LONG)).isFalse();

        rights.setAllowed(BLACK_LONG);
        assertThat(rights.isAllowed(WHITE_SHORT)).isFalse();
        assertThat(rights.isAllowed(BLACK_SHORT)).isTrue();
        assertThat(rights.isAllowed(WHITE_LONG)).isTrue();
        assertThat(rights.isAllowed(BLACK_LONG)).isTrue();
    }
}