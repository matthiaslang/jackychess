package org.mattlang.jc.engine.search;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.Color;

public class KillerMovesTest {

    @Test
    public void simpleTest(){
        KillerMoves km = new KillerMoves();
        km.addKiller(Color.WHITE, 7, 1);
        Assertions.assertThat(km.isKiller(Color.WHITE, 7, 1)).isTrue();
        Assertions.assertThat(km.isKiller(Color.WHITE, 1, 1)).isFalse();

        km.addKiller(Color.WHITE, 9, 1);
        Assertions.assertThat(km.isKiller(Color.WHITE, 7, 1)).isTrue();
        Assertions.assertThat(km.isKiller(Color.WHITE, 9, 1)).isTrue();

        km.addKiller(Color.WHITE, 11, 1);
        Assertions.assertThat(km.isKiller(Color.WHITE, 7, 1)).isFalse();
        Assertions.assertThat(km.isKiller(Color.WHITE, 9, 1)).isTrue();
        Assertions.assertThat(km.isKiller(Color.WHITE, 11, 1)).isTrue();
    }

}