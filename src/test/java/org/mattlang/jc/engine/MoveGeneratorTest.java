package org.mattlang.jc.engine;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.moves.MoveImpl;

public class MoveGeneratorTest {

    @Test
    public void testPawnPromotionAndUndoing1() {
        BoardRepresentation board = new BitBoard();
        String fen = "position fen 8/P7/8/2K5/5k2/8/p7/8 b k - 2 17 ";
        board.setFenPosition(fen);

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new BBMoveGeneratorImpl();
        MoveList whiteMoves = generator.generate(board, WHITE);

        MoveGenerator generator2 = new BBMoveGeneratorImpl();
        MoveList blackMoves = generator2.generate(board, BLACK);

        List<MoveImpl> wMoves = whiteMoves.extractList();
        // 8 king moves + 4 promotion moves
        assertThat(wMoves.size()).isEqualTo(12);
        List<MoveImpl> bMoves = blackMoves.extractList();
        // 8 king moves + 4 promotion moves
        assertThat(bMoves.size()).isEqualTo(12);

        Move wQPromotion = wMoves.stream().filter(m -> m.isPromotion() && m.getPromotedFigure() == Figure.W_Queen).findFirst().get();
        Move bQPromotion = bMoves.stream().filter(m -> m.isPromotion() && m.getPromotedFigure() == Figure.B_Queen).findFirst().get();

        board.domove(wQPromotion);
        board.domove(bQPromotion);

        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigure(parsePos("a8"))).isEqualTo(Figure.W_Queen);
        assertThat(board.getFigure(parsePos("a1"))).isEqualTo(Figure.B_Queen);

        // undoing:
        board.undo(bQPromotion);
        board.undo(wQPromotion);

        System.out.println(board.toUniCodeStr());
        BoardRepresentation cmpboard = new BitBoard();
        cmpboard.setFenPosition(fen);

        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());

    }

    @Test
    public void testPawnPromotionAndUndoing2() {

        BoardRepresentation board = new BitBoard();
        String fen = "position fen pp6/P7/8/8/8/8/p7/PP6 b k - 2 17 ";
        board.setFenPosition(fen);

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new BBMoveGeneratorImpl();
        MoveList whiteMoves = generator.generate(board, WHITE);
        // we are interested in the pawn at a7 which gets promoted to a queen:
        Move a7PawnMove = whiteMoves.extractList()
                .stream()
                .filter(m -> m.toStr().startsWith("a7") && m.isPromotion() && m.getPromotedFigure() == Figure.W_Queen)
                .findAny()
                .get();

        board.domove(a7PawnMove);

        assertThat(board.getFigure(parsePos("b8"))).isEqualTo(Figure.W_Queen);
        // undoing:
        board.undo(a7PawnMove);

        BoardRepresentation cmpboard = new BitBoard();
        cmpboard.setFenPosition(fen);
        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());

        MoveGenerator generator2 = new BBMoveGeneratorImpl();
        MoveList blackMoves = generator2.generate(board, BLACK);
        Move a2PawnMove = blackMoves.extractList()
                .stream()
                .filter(m -> m.toStr().startsWith("a2") && m.isPromotion() && m.getPromotedFigure() == Figure.B_Queen)
                .findAny()
                .get();

        board.domove(a2PawnMove);

        assertThat(board.getFigure(parsePos("b1"))).isEqualTo(Figure.B_Queen);

        // undoing:
        board.undo(a2PawnMove);
        assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }

    @Test
    public void testMoveGenFromStartWhite() {

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        MoveGenerator moveGenerator = new BBMoveGeneratorImpl();
        MoveList moves = moveGenerator.generate(board, WHITE);

        List<MoveImpl> mmoves = moves.extractList();

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

    @Test
    public void testRochade() {

        BoardRepresentation board = new BitBoard();
        String fen = "position fen r3k2r/8/8/8/8/8/8/R3K2R b k - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        BoardRepresentation cmpboard = new BitBoard();
        cmpboard.setFenPosition(fen);

        MoveGenerator moveGenerator = new BBMoveGeneratorImpl();
        MoveList moves = moveGenerator.generate(board, WHITE);
        // find white rochade moves:
        List<Move> rochMoves = moves.extractList().stream()
                .filter(m -> m.toStr().equals("e1c1") || m.toStr().equals("e1g1"))
                .collect(toList());
        assertThat(rochMoves.size()).isEqualTo(2);

        for (Move rochMove : rochMoves) {
            board.domove(rochMove);
            board.undo(rochMove);
            assertThat(board.toUniCodeStr())

                    .withFailMessage(
                            "wrong undoing " + rochMove.toStr() + " ending with board \n" + board.toUniCodeStr())
                    .isEqualTo(cmpboard.toUniCodeStr());
        }

    }

    @Test
    public void enPassant() {
        BoardRepresentation board = new BitBoard();
        String fen = "position fen k7/3p4/8/2P1P3/2p1p3/8/3P4/K7 b k - 2 17 ";
        board.setFenPosition(fen);

        BoardRepresentation copy = board.copy();

        System.out.println(board.toUniCodeStr());

        MoveGenerator generator = new BBMoveGeneratorImpl();
        MoveList whiteMoves = generator.generate(board, WHITE);

        List<MoveImpl> wMoves = whiteMoves.extractList();
        // 4 moves + 3 king moves
        assertThat(wMoves.size()).isEqualTo(7);
        Optional<MoveImpl> wPawnDoubleMove = wMoves.stream().filter(m -> m.toStr().equals("d2d4")).findFirst();
        assertThat(wPawnDoubleMove).isNotEmpty();

        // execute the double move:
        board.domove(wPawnDoubleMove.get());

        // now black moves should have two en passant moves:
        MoveList blackMoves = generator.generate(board, BLACK);

        // 6 moves + 3 king moves
        assertThat(blackMoves.size()).isEqualTo(9);

        Optional<MoveImpl> epMove1 = blackMoves.extractList().stream().filter(m -> m.toStr().equals("e4d3")).findFirst();

        Optional<MoveImpl> epMove2 = blackMoves.extractList().stream().filter(m -> m.toStr().equals("c4d3")).findFirst();

        board.domove(epMove1.get());

        // try undoing the moves:
        board.undo(epMove1.get());
        board.undo(wPawnDoubleMove.get());

        assertThat(board.toUniCodeStr()).isEqualTo(copy.toUniCodeStr());
    }

    @Test
    public void enPassantFromFEN() {
        BoardRepresentation board = new BitBoard();
        String fen = "position fen k7/3p4/8/2P1P3/2pPp3/8/8/K7 b k d3 2 17 ";
        board.setFenPosition(fen);

        BoardRepresentation copy = board.copy();

        System.out.println(board.toUniCodeStr());
        MoveGenerator generator = new BBMoveGeneratorImpl();

        // now black moves should have two en passant moves:
        MoveList blackMoves = generator.generate(board, BLACK);

        // 6 moves + 3 king moves
        assertThat(blackMoves.size()).isEqualTo(9);

        Optional<MoveImpl> epMove1 = blackMoves.extractList().stream().filter(m -> m.toStr().equals("e4d3")).findFirst();

        Optional<MoveImpl> epMove2 = blackMoves.extractList().stream().filter(m -> m.toStr().equals("c4d3")).findFirst();

        board.domove(epMove1.get());

        // try undoing the moves:
        board.undo(epMove1.get());

        assertThat(board.toUniCodeStr()).isEqualTo(copy.toUniCodeStr());
    }
}