package org.mattlang.jc.engine.search;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.moves.MoveImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.nWhite;

class HistoryHeuristicTest {

    @Test
    public void testit() {
        HistoryHeuristic hh = new HistoryHeuristic();
        hh.reset();

        Move move = new MoveImpl("a2a4");
        hh.update(nWhite, move, 3);

        assertThat(hh.calcValue(move, nWhite)).isEqualTo(600);
        hh.update(nWhite, move, 2);
        assertThat(hh.calcValue(move, nWhite)).isEqualTo(971);

        hh.updateBad(nWhite, move, 3);
        assertThat(hh.calcValue(move, nWhite)).isEqualTo(300);

        hh.reset();
        hh.update(nWhite, move, 40);
        assertThat(hh.calcValue(move, nWhite)).isEqualTo(1200);
        for (int i = 0; i < 33; i++) {
            hh.update(nWhite, move, 40);
        }

        assertThat(hh.calcValue(move, nWhite)).isEqualTo(8158);
    }


    @Test
    public void captureHist() {
        CaptureHeuristic hh = new CaptureHeuristic();
        hh.reset();

        Move move = new MoveImpl("a2a4");
        hh.update(nWhite, move, 3);

        assertThat(hh.calcValue(move, nWhite)).isEqualTo(600);
        hh.update(nWhite, move, 2);
        assertThat(hh.calcValue(move, nWhite)).isEqualTo(971);

        hh.updateBad(nWhite, move, 3);
        assertThat(hh.calcValue(move, nWhite)).isEqualTo(300);

        hh.reset();
        hh.update(nWhite, move, 40);
        assertThat(hh.calcValue(move, nWhite)).isEqualTo(1200);
        for (int i = 0; i < 33; i++) {
            hh.update(nWhite, move, 40);
        }

        assertThat(hh.calcValue(move, nWhite)).isEqualTo(8158);
    }


    @Test
    public void contHistory() {
        ContinuationHistoryHeuristic hh = new ContinuationHistoryHeuristic();
        hh.reset();

        Move move = new MoveImpl("a2a4");
        hh.update(nWhite, move.getMoveInt(), move, 3);

        assertThat(hh.calcValue(move.getMoveInt(), move, nWhite)).isEqualTo(600);
        hh.update(nWhite, move.getMoveInt(), move, 2);
        assertThat(hh.calcValue(move.getMoveInt(), move, nWhite)).isEqualTo(971);

        hh.updateBad(nWhite, move.getMoveInt(), move, 3);
        assertThat(hh.calcValue(move.getMoveInt(), move, nWhite)).isEqualTo(300);

        hh.reset();
        hh.update(nWhite, move.getMoveInt(), move, 40);
        assertThat(hh.calcValue(move.getMoveInt(), move, nWhite)).isEqualTo(1200);

        for (int i = 0; i < 33; i++) {
            hh.update(nWhite, move.getMoveInt(), move, 40);
        }

        assertThat(hh.calcValue(move.getMoveInt(), move, nWhite)).isEqualTo(8158);
    }
}