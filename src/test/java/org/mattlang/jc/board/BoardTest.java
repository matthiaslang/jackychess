package org.mattlang.jc.board;

import static org.mattlang.jc.board.FigureConstants.FT_KNIGHT;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.moves.MoveImpl;

public class BoardTest {

    @Test
    public void testStartPosition() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testStartPositionWithMoves() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        board.domove(new MoveImpl(FT_PAWN, parsePos("e2"), parsePos("e4"), (byte) 0));
        board.domove(new MoveImpl(FT_PAWN, parsePos("e7"), parsePos("e5"), (byte) 0));
        board.domove(new MoveImpl(FT_KNIGHT, parsePos("g1"), parsePos("f3"), (byte) 0));
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testUndoingMoves() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        System.out.println("doing moves...");
        Move m1 = new MoveImpl(FT_PAWN, parsePos("e2"), parsePos("e4"), (byte) 0);
        board.domove(m1);
        Move m2 = new MoveImpl(FT_PAWN, parsePos("e7"), parsePos("e5"), (byte) 0);
        board.domove(m2);
        Move m3 = new MoveImpl(FT_KNIGHT, parsePos("g1"), parsePos("f3"), (byte) 0);
        board.domove(m3);
        System.out.println(board.toUniCodeStr());

        System.out.println("undoing moves...");
        board.undo(m3);
        board.undo(m2);
        board.undo(m1);

        System.out.println(board.toUniCodeStr());

        BoardRepresentation cmpboard = new BitBoard();
        cmpboard.setStartPosition();
        Assertions.assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }

}