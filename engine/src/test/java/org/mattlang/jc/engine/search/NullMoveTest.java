package org.mattlang.jc.engine.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.Configurator;

public class NullMoveTest {

    @Test
    public void testNoZugzwangCriteria() {

        BoardRepresentation board = Configurator.createBoard();
        //        GameState gameState = board.setFenPosition("position fen RN6/8/rnb5/4K3/8/6k1/8/3q4 w - - 14 1 ");
        GameState gameState = board.setFenPosition("position fen 8/8/rnb5/4KP2/8/6k1/8/3q4 w - - 14 1 ");
        board.println();

        //        Assertions.assertThat(PhaseCalculator.isOpeningOrMiddleGame(gameState.getBoard())).isTrue();

        assertThat(SearchContext.isNoZugwang(gameState.getBoard())).isFalse();

         gameState = board.setFenPosition("position fen 8/8/rnb5/4KP2/8/6k1/8/3q4 b - - 14 1 ");
        board.println();

        assertThat(SearchContext.isNoZugwang(gameState.getBoard())).isTrue();
    }

}
