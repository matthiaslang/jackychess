package org.mattlang.jc.moves;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Color;

public class MoveListImplTest {

    @Test
    public void testFilterMove() {
        MoveListImpl moveList = new MoveListImpl();
        moveList.addFilter(5);
        moveList.addFilter(50);
        moveList.addFilter(500);

        moveList.addMove(5);
        assertThat(moveList.size()).isEqualTo(0);

        moveList.addMove(7);
        moveList.addMove(9);
        assertThat(moveList.size()).isEqualTo(2);

        moveList.addMove(50);
        moveList.addMove(500);
        assertThat(moveList.size()).isEqualTo(2);

        moveList.reset(Color.WHITE);
        moveList.addMove(50);
        assertThat(moveList.size()).isEqualTo(1);

    }
}