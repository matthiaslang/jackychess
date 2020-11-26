package org.mattlang.jc.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Move.parsePos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.engine.MoveGenerator;

public class BoardTest {

    @Test
    public void testStartPosition() {
        Board board = new Board();
        board.setStartPosition();
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testStartPositionWithMoves() {
        Board board = new Board();
        board.setStartPosition();

        board.move(new Move("e2e4"));
        board.move(new Move("e7e5"));
        board.move(new Move("g1f3"));
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testUndoingMoves() {
        Board board = new Board();
        board.setStartPosition();

        System.out.println("doing moves...");
        UndoMove m1 = board.move(new Move("e2e4"));
        UndoMove m2 = board.move(new Move("e7e5"));
        UndoMove m3 = board.move(new Move("g1f3"));
        System.out.println(board.toUniCodeStr());

        System.out.println("undoing moves...");
        board.move(m3);
        board.move(m2);
        board.move(m1);

        System.out.println(board.toUniCodeStr());

        Board cmpboard = new Board();
        cmpboard.setStartPosition();
        Assertions.assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }

    @Test
    public void testPawnPromotionAndUndoing1() {
        Board board = new Board();
        String fen = "position fen 8/P7/8/8/8/8/p7/8 b k - 2 17 ";
        board.setFenPosition(fen);

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new MoveGenerator();
        List<Move> whiteMoves = generator.generate(board, WHITE);

        MoveGenerator generator2 = new MoveGenerator();
        List<Move> blackMoves = generator2.generate(board, BLACK);

        List<UndoMove> undoes = new ArrayList<>();
        for (Move move : whiteMoves) {
            undoes.add(board.move(move));
        }
        for (Move move : blackMoves) {
            undoes.add(board.move(move));
        }
        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigure(parsePos("a8"))).isEqualTo(Figure.Queen);
        assertThat(board.getFigure(parsePos("a1"))).isEqualTo(Figure.B_Queen);

        // undoing:
        for (UndoMove undo : undoes) {
            board.move(undo);
        }
        System.out.println(board.toUniCodeStr());
        Board cmpboard = new Board();
        cmpboard.setFenPosition(fen);

        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }

    @Test
    public void testPawnPromotionAndUndoing2() {
        Board board = new Board();
        String fen = "position fen pp6/P7/8/8/8/8/p7/PP6 b k - 2 17 ";
        board.setFenPosition(fen);

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new MoveGenerator();
        List<Move> whiteMoves = generator.generate(board, WHITE);
        // we are interested in the pawn at a7 which gets promoted to a queen:
        Move a7PawnMove = whiteMoves.stream().filter(m -> m.toStr().startsWith("a7")).findAny().get();

        UndoMove undo = board.move(a7PawnMove);

        assertThat(board.getFigure(parsePos("b8"))).isEqualTo(Figure.Queen);
        // undoing:
        board.move(undo);

        Board cmpboard = new Board();
        cmpboard.setFenPosition(fen);
        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());

        MoveGenerator generator2 = new MoveGenerator();
        List<Move> blackMoves = generator2.generate(board, BLACK);
        Move a2PawnMove = blackMoves.stream().filter(m -> m.toStr().startsWith("a2")).findAny().get();

        undo = board.move(a2PawnMove);

        assertThat(board.getFigure(parsePos("b1"))).isEqualTo(Figure.B_Queen);

        // undoing:
        board.move(undo);
        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }

    @Test
    public void testMoveGenFromStartWhite() {
        Board board = new Board();
        board.setStartPosition();

        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> moves = moveGenerator.generate(board, WHITE);

        assertThat(moves)
                .flatExtracting((Function<? super Move, ?>) m -> m.toStr())
                .containsExactlyInAnyOrder(
                        "b1a3",
                        "b1c3",
                        "g1f3",
                        "g1h3",
                        "a2a3",
                        "a2a4",
                        "b2b3",
                        "b2b4",
                        "c2c3",
                        "c2c4",
                        "d2d3",
                        "d2d4",
                        "e2e3",
                        "e2e4",
                        "f2f3",
                        "f2f4",
                        "g2g3",
                        "g2g4",
                        "h2h3",
                        "h2h4"
                );

    }

}