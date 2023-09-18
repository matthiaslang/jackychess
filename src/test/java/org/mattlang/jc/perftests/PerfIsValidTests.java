package org.mattlang.jc.perftests;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveCursor;

/**
 * PerfTests to validate the boards.isValid method.
 */
public class PerfIsValidTests {

    private boolean debug = false;
    private PerftConsumer validtMoveTester = new PerftConsumer() {

        @Override
        public void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor) {
            // undo the currently done move to have the position before the legal move has been executed:
            cursor.undoMove(board);
            // now we can test the validity of the move:
            Assertions.assertThat(board.isvalidmove(color, cursor.getMoveInt()))
//                    .withFailMessage("on Board %s: Move %s is not valid!",
//                            board.toUniCodeStr(),
//                            new MoveImpl(cursor.getMoveInt()).toStr())
                    .isTrue();

            // redo the move to get back in the state that the move is done
            cursor.move(board);
        }
    };

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Factory.setDefaults(Factory.createStable());
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);

        perft.perftInitialPosition();

    }



    @Test
    public void position2() {
        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);

        perft.position2();

    }

    @Test
    public void position3() {
        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);

        perft.position3();
    }

    @Test
    public void position4() {
        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);

        perft.position4();
    }

}
