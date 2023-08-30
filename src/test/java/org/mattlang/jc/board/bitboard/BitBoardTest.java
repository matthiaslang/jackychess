package org.mattlang.jc.board.bitboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.MoveListImpl;

public class BitBoardTest {

    @Test
    public void testCopy() {
        BitBoard board = new BitBoard();
        board.setChess960(true);
        assertThat(board.copy().isChess960()).isTrue();

        board.setChess960(false);
        assertThat(board.copy().isChess960()).isFalse();
    }

    @Test
    public void isValidMoveTest() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        PseudoLegalMoveGenerator movegen = new PseudoLegalMoveGenerator();

        CheckChecker checkChecker = new BBCheckCheckerImpl();

        // positive check, that all legal moves are "valid" moves:
        MoveList moveList = new MoveListImpl();
        moveList.reset(Color.WHITE);
        movegen.generate(board, Color.WHITE, moveList);

        try (MoveBoardIterator iterator = moveList.iterateMoves(board, checkChecker)) {
            while (iterator.nextMove()) {
                assertThat(board.isvalidmove(Color.WHITE, iterator.getMoveInt())).isTrue();
            }
        }

        moveList.reset(Color.BLACK);
        movegen.generate(board, Color.BLACK, moveList);

        try (MoveBoardIterator iterator = moveList.iterateMoves(board, checkChecker)) {
            while (iterator.nextMove()) {
                assertThat(board.isvalidmove(Color.BLACK, iterator.getMoveInt())).isTrue();
            }
        }

        // check now negative cases:

        // 1. from is wrong:
        int move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("g3"), parsePos("g4"), FT_EMPTY);
        assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

        // 2. from is right, but different color:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("g4"), FT_EMPTY);
        assertThat(board.isvalidmove(Color.BLACK, move)).isFalse();

        // 3. to is our own figure:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f2"), FT_EMPTY);
        assertThat(board.isvalidmove(Color.BLACK, move)).isFalse();

        // 3. to is our own figure and has capture set:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f2"), Figure.B_Pawn.figureCode);
        assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

        // 3. to is empty but denotes a "wrong" figure:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("g4"), Figure.B_Pawn.figureCode);
        assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

        // 3. capture but denotes a "wrong" opponent figure:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f6"), Figure.B_Pawn.figureCode);
        assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

        // 3. capture but denotes a "right" opponent figure:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f6"), Figure.B_Knight.figureCode);
        assertThat(board.isvalidmove(Color.WHITE, move)).isTrue();

        // 4. capture of slighting figure where figures are inbetween:
        move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f7"), Figure.B_Pawn.figureCode);
        assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

        // 4. inbetween check double pawn push:
        move = MoveImpl.createNormalMove(FT_PAWN, parsePos("f2"), parsePos("f4"), FT_EMPTY);
        assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

        // 4. korrekt double pawn push:
        move = MoveImpl.createNormalMove(FT_PAWN, parsePos("g2"), parsePos("g4"), FT_EMPTY);
        assertThat(board.isvalidmove(Color.WHITE, move)).isTrue();
    }

    /**
     * Simple performance test of isvalidmove.
     */
    @Ignore
    @Test
    public void validMovePerfTest() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        int ROUNDS=100000000;
        for (int i = 0; i < ROUNDS; i++) {

            // 1. from is wrong:
            int move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("g3"), parsePos("g4"), FT_EMPTY);
            assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

            // 2. from is right, but different color:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("g4"), FT_EMPTY);
            assertThat(board.isvalidmove(Color.BLACK, move)).isFalse();

            // 3. to is our own figure:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f2"), FT_EMPTY);
            assertThat(board.isvalidmove(Color.BLACK, move)).isFalse();

            // 3. to is our own figure and has capture set:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f2"), Figure.B_Pawn.figureCode);
            assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

            // 3. to is empty but denotes a "wrong" figure:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("g4"), Figure.B_Pawn.figureCode);
            assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

            // 3. capture but denotes a "wrong" opponent figure:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f6"), Figure.B_Pawn.figureCode);
            assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

            // 3. capture but denotes a "right" opponent figure:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f6"), Figure.B_Knight.figureCode);
            assertThat(board.isvalidmove(Color.WHITE, move)).isTrue();

            // 4. capture of slighting figure where figures are inbetween:
            move = MoveImpl.createNormalMove(FT_QUEEN, parsePos("f3"), parsePos("f7"), Figure.B_Pawn.figureCode);
            assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

            // 4. inbetween check double pawn push:
            move = MoveImpl.createNormalMove(FT_PAWN, parsePos("f2"), parsePos("f4"), FT_EMPTY);
            assertThat(board.isvalidmove(Color.WHITE, move)).isFalse();

            // 4. korrekt double pawn push:
            move = MoveImpl.createNormalMove(FT_PAWN, parsePos("g2"), parsePos("g4"), FT_EMPTY);
            assertThat(board.isvalidmove(Color.WHITE, move)).isTrue();
        }
    }

}