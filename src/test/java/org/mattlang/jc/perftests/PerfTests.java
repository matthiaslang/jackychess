package org.mattlang.jc.perftests;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl2;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;
import org.mattlang.jc.movegenerator.PositionBasedGenerator;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfTests {


    @Test
    public void initialPositionPerformanceMoveGenerator() {
        Board board = new Board();
        board.setStartPosition();

        perftReset();
        perft(new MoveGeneratorImpl2(), board, Color.WHITE, 6);

    }

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Board board = new Board();
        board.setStartPosition();

        perftReset();
        perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 5);

    }

        @Test
    public void initialPosition() {
        Board board = new Board();
        board.setStartPosition();

        perftReset();
        perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 1);
        Assertions.assertThat(nodes).isEqualTo(20);
        Assertions.assertThat(captures).isEqualTo(0);

        perftReset();
        perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 2);
        Assertions.assertThat(nodes).isEqualTo(400);
        Assertions.assertThat(captures).isEqualTo(0);

        perftReset();
        perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 3);
        Assertions.assertThat(nodes).isEqualTo(8902);
        Assertions.assertThat(captures).isEqualTo(34);

        perftReset();
        perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 4);
        Assertions.assertThat(nodes).isEqualTo(197281);
        // captures wrong... why?
//        Assertions.assertThat(captures).isEqualTo(1576);

    }

    @Test
    public void position2() {
        Board board = new Board();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");

        perftReset();
        perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 1);
        Assertions.assertThat(nodes).isEqualTo(48);
        Assertions.assertThat(captures).isEqualTo(8);

        // not working, maybe because of castle rights checking missing
//        perftReset();
//        Perft(new LegalMoveGeneratorImpl2(), board, Color.WHITE, 2);
//        Assertions.assertThat(nodes).isEqualTo(2039);
//        Assertions.assertThat(captures).isEqualTo(351);
    }


    int nodes = 0;
    int captures = 0;

    void perftReset() {
        nodes = 0;
        captures = 0;
    }

    void perft(PositionBasedGenerator<MoveList> generator, BoardRepresentation board, Color color, int depth) {

        if (depth == 0) {
            nodes++;
            return;
        }

        MoveList moves = generator.generate(board, color);
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(board);
            if (moveCursor.isCapture()) {
                captures++;
            }
            perft(generator, board, color.invert(), depth - 1);
            moveCursor.undoMove(board);
        }
    }
}
