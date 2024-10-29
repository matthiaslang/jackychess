package org.mattlang.jc.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.*;

import org.junit.Test;

public class ColorTest {

    @Test
    public void testInvert() {
        assertThat(invert(nWhite)).isEqualTo(nBlack);
        assertThat(invert(nBlack)).isEqualTo(nWhite);

    }
}