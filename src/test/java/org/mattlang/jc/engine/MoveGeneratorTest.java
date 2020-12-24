package org.mattlang.jc.engine;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.PawnPromotionMove;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.IndexConversion.parsePos;

// todo refactor wiht both MoveGeneratorCompactListTest...
public class MoveGeneratorTest {

    @Test
    public void testPawnPromotionAndUndoing1() {
        Board board = new Board();
        String fen = "position fen 8/P7/8/8/8/8/p7/8 b k - 2 17 ";
        board.setFenPosition(fen);

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new MoveGeneratorImpl();
        MoveList whiteMoves = generator.generate(board, WHITE);

        MoveGenerator generator2 = new MoveGeneratorImpl();
        MoveList blackMoves = generator2.generate(board, BLACK);

        List<Move> wMoves = createMoveList(whiteMoves);
        assertThat(wMoves.size()).isEqualTo(4);
        List<Move> bMoves = createMoveList(blackMoves);
        assertThat(bMoves.size()).isEqualTo(4);

        Move wQPromotion = wMoves.stream().filter(m -> ((PawnPromotionMove) m).getPromotedFigure() == Figure.W_Queen).findFirst().get();
        Move bQPromotion = bMoves.stream().filter(m -> ((PawnPromotionMove) m).getPromotedFigure() == Figure.B_Queen).findFirst().get();
        List<Move> undoes = new ArrayList<>();

        undoes.add(board.move(wQPromotion));
        undoes.add(board.move(bQPromotion));

        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigure(parsePos("a8"))).isEqualTo(Figure.W_Queen);
        assertThat(board.getFigure(parsePos("a1"))).isEqualTo(Figure.B_Queen);

        // undoing:
        for (Move undo : undoes) {
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

        MoveGenerator generator = new MoveGeneratorImpl();
        MoveList whiteMoves = generator.generate(board, WHITE);
        // we are interested in the pawn at a7 which gets promoted to a queen:
        Move a7PawnMove = createMoveList(whiteMoves).stream().filter(m -> m.toStr().startsWith("a7")).findAny().get();

        Move undo = board.move(a7PawnMove);

        assertThat(board.getFigure(parsePos("b8"))).isEqualTo(Figure.W_Queen);
        // undoing:
        board.move(undo);

        Board cmpboard = new Board();
        cmpboard.setFenPosition(fen);
        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());

        MoveGenerator generator2 = new MoveGeneratorImpl();
        MoveList blackMoves = generator2.generate(board, BLACK);
        Move a2PawnMove = createMoveList(blackMoves).stream().filter(m -> m.toStr().startsWith("a2")).findAny().get();

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

        MoveGenerator moveGenerator = new MoveGeneratorImpl();
        MoveList moves = moveGenerator.generate(board, WHITE);

        List<Move> mmoves = createMoveList(moves);

        assertThat(mmoves)
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

    /**
     * Helper method to create a list of individual move objects for tests to better inspect the results.
     *
     * @param moves
     * @return
     */
    private List<Move> createMoveList(MoveList moves) {
        ArrayList<Move> result = new ArrayList<>();
        for (MoveCursor move : moves) {
            result.add(move.getMove());
        }
        return result;
    }

    @Test
    public void testRochade() {

        Board board = new Board();
        String fen = "position fen r3k2r/8/8/8/8/8/8/R3K2R b k - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        Board cmpboard = new Board();
        cmpboard.setFenPosition(fen);

        MoveGenerator moveGenerator = new MoveGeneratorImpl();
        MoveList moves = moveGenerator.generate(board, WHITE);
        // find white rochade moves:
        List<Move> rochMoves = createMoveList(moves).stream()
                .filter(m -> m.toStr().equals("e1c1") || m.toStr().equals("e1g1"))
                .collect(toList());
        assertThat(rochMoves.size()).isEqualTo(2);

        for (Move rochMove : rochMoves) {
            Move undoer = board.move(rochMove);
            board.move(undoer);
            assertThat(board.toUniCodeStr())

                    .withFailMessage(
                            "wrong undoing " + rochMove.toStr() + " ending with board \n" + board.toUniCodeStr())
                    .isEqualTo(cmpboard.toUniCodeStr());
        }

    }

    @Test
    public void enPassant() {
        Board board = new Board();
        String fen = "position fen 8/3p4/8/2P1P3/2p1p3/8/3P4/8 b k - 2 17 ";
        board.setFenPosition(fen);

        Board copy = board.copy();

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new MoveGeneratorImpl();
        MoveList whiteMoves = generator.generate(board, WHITE);

        List<Move> wMoves = createMoveList(whiteMoves);
        assertThat(wMoves.size()).isEqualTo(4);
        Optional<Move> wPawnDoubleMove = wMoves.stream().filter(m -> m.toStr().equals("d2d4")).findFirst();
        assertThat(wPawnDoubleMove).isNotEmpty();

        // execute the double move:
        Move undoer = board.move(wPawnDoubleMove.get());

        // now black moves should have two en passant moves:
        MoveList blackMoves = generator.generate(board, BLACK);

        assertThat(blackMoves.size()).isEqualTo(6);

        Optional<Move> epMove1 = createMoveList(blackMoves).stream().filter(m -> m.toStr().equals("e4d3")).findFirst();

        Optional<Move> epMove2 = createMoveList(blackMoves).stream().filter(m -> m.toStr().equals("c4d3")).findFirst();

        Move undoEP = board.move(epMove1.get());

        // try undoing the moves:
        board.move(undoEP);
        board.move(undoer);

        assertThat(board.toUniCodeStr()).isEqualTo(copy.toUniCodeStr());
    }

    @Test
    public void enPassantFromFEN() {
        Board board = new Board();
        String fen = "position fen 8/3p4/8/2P1P3/2pPp3/8/8/8 b k d3 2 17 ";
        board.setFenPosition(fen);

        Board copy = board.copy();

        System.out.println(board.toUniCodeStr());
        MoveGenerator generator = new MoveGeneratorImpl();

        // now black moves should have two en passant moves:
        MoveList blackMoves = generator.generate(board, BLACK);

        assertThat(blackMoves.size()).isEqualTo(6);

        Optional<Move> epMove1 = createMoveList(blackMoves).stream().filter(m -> m.toStr().equals("e4d3")).findFirst();

        Optional<Move> epMove2 = createMoveList(blackMoves).stream().filter(m -> m.toStr().equals("c4d3")).findFirst();

        Move undoEP = board.move(epMove1.get());

        // try undoing the moves:
        board.move(undoEP);

        assertThat(board.toUniCodeStr()).isEqualTo(copy.toUniCodeStr());
    }
}