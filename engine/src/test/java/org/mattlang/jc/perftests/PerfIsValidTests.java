package org.mattlang.jc.perftests;

import static org.mattlang.jc.perftests.PerfTests.DEFAULT_SUPPLIER;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.perft.Perft;
import org.mattlang.jc.perft.PerftConsumer;

/**
 * PerfTests to validate the boards.isValid method.
 */
@Category(SlowTests.class)
public class PerfIsValidTests {

    private boolean debug = false;
    private PerftConsumer validtMoveTester = new PerftConsumer() {

        @Override
        public void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor) {
            // undo the currently done move to have the position before the legal move has been executed:
            board.undo(cursor);

            // now we can test the validity of the move:
            Assertions.assertThat(board.isvalidmove(color, cursor.getMoveInt()))
                    //                    .withFailMessage("on Board %s: Move %s is not valid!",
                    //                            board.toUniCodeStr(),
                    //                            new MoveImpl(cursor.getMoveInt()).toStr())
                    .isTrue();

            // redo the move to get back in the state that the move is done
            board.domove(cursor);
        }
    };

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Factory.setDefaults(Factory.createStable());
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(validtMoveTester);

        perft.perftInitialPosition();

    }

    @Test
    public void position2() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(validtMoveTester);

        perft.position2();

    }

    @Test
    public void position3() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(validtMoveTester);

        perft.position3();
    }

    @Test
    public void position4() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(validtMoveTester);

        perft.position4();
    }

}
