package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.BB.*;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPawnEvaluation.getPawnNeighbours;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;
import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.bitboard.BitBoard;

public class ParameterizedPawnEvaluationTest {

    private static final Representation BITREP = new StandardRepresentation() {

        @Override
        public String toStringOf(Object object) {
            if (object instanceof Long) {
                return BB.toStrBoard((Long) object);
            }
            return super.toStringOf(object);
        }

        @Override
        public String unambiguousToStringOf(Object object) {
            return super.unambiguousToStringOf(object);
        }
    };

    @Test
    public void testPawnNeighbors() {

        BitBoard board = new BitBoard();
        board.setStartPosition();

        long neighbours = getPawnNeighbours(board.getBoard().getPawns(nWhite));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(B2 | C2 | D2 | E2 | F2 | G2);

        neighbours = getPawnNeighbours(board.getBoard().getPawns(nBlack));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(B7 | C7 | D7 | E7 | F7 | G7);
    }

    @Test
    public void testSinglePawnNeighbors() {

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen K7/8/4p3/8/8/4PP2/8/7k w - - 1 56 ");
        board.println();

        long neighbours = getPawnNeighbours(board.getBoard().getPawns(nWhite));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(D3 | E3 | F3 | G3);

        neighbours = getPawnNeighbours(board.getBoard().getPawns(nBlack));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(D6 | F6);
    }

    @Test
    public void testPawnNeighbors2() {

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen K7/8/p1p1p1p1/8/8/1P1P1P1P/8/7k w - - 1 56 ");
        board.println();

        long neighbours = getPawnNeighbours(board.getBoard().getPawns(nWhite));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(C3 | E3 | G3);

        neighbours = getPawnNeighbours(board.getBoard().getPawns(nBlack));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(B6 | D6 | F6);
    }

    @Test
    public void testPawnNeighbors3() {

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen K7/8/4p3/8/8/3P4/8/7k w - - 1 56 ");
        board.println();

        long neighbours = getPawnNeighbours(board.getBoard().getPawns(nWhite));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(C3 | E3);

        neighbours = getPawnNeighbours(board.getBoard().getPawns(nBlack));
        assertThat(neighbours).withRepresentation(BITREP).isEqualTo(D6 | F6);
    }

}