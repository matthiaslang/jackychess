package org.mattlang.jc.engine.search;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class KillerMovesTest {

    @Test
    public void simpleTest() {
        KillerMoves km = new KillerMoves();
        km.addKiller(7, 1);
        Assertions.assertThat(km.isKiller(7, 1)).isTrue();
        Assertions.assertThat(km.isKiller(1, 1)).isFalse();

        km.addKiller(9, 1);
        Assertions.assertThat(km.isKiller(7, 1)).isTrue();
        Assertions.assertThat(km.isKiller(9, 1)).isTrue();

        km.addKiller(11, 1);
        Assertions.assertThat(km.isKiller(7, 1)).isFalse();
        Assertions.assertThat(km.isKiller(9, 1)).isTrue();
        Assertions.assertThat(km.isKiller(11, 1)).isTrue();
    }

}