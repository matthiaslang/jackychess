package org.mattlang.jc.engine.evaluation.parameval;

import org.junit.Test;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;

public class PassedPawnEvalTest {

    @Test
    public void testUnStoppableIn1Moves() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen k7/4P3/8/8/K7/8/8/8 w - - 2 17 ");
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);
    }

    @Test
    public void testUnStoppableIn2Moves() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen k7/8/4P3/8/K7/8/8/8 w - - 2 17 ");
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);
    }

    @Test
    public void testKingInFront() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 3k4/8/4P3/8/K7/8/8/8 w - - 2 17 ");
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);
    }

    @Test
    public void testAttacksOnPath() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen k7/8/2b1P3/8/K7/8/8/8 w - - 2 17 ");
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);
    }

    @Test
    public void testBishopCantStopItButAttacks() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen k7/4P3/3b4/8/K7/8/8/8 w - - 2 17 ");
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);
    }

    @Test
    public void testBishopCantStopIt() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen k7/4P3/1b6/8/K7/8/8/8 w - - 2 17 ");
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);
    }

    private static int calcPassedPawnEval(BitBoard board) {
        PassedPawnEval ppe = new PassedPawnEval();
        ppe.setMultiplierAttacked(2);
        ppe.setMultiplierBlocked(3);
        ppe.setMultiplierAttackedByRookFromBehind(5);
        ppe.setMultiplierNextSquareAttacked(7);
        ppe.setMultiplierEnemyKingInFront(9);
        ppe.setMultiplierDefendedByRookFromBehind(11);
        ppe.setMultiplierNoEnemyAttacksInFront(13);
        ppe.setMultiplierNextSquareDefended(17);
        ppe.setPassedScoreEg(new ArrayFunction(new int[] { 0, 2, 4, 8, 16, 32, 64 }));
        ppe.setPassedKingMulti(new FloatArrayFunction(new float[] { 0, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f }));

        long whitePawns = board.getBoard().getPawns(BitChessBoard.nWhite);
        long blackPawns = board.getBoard().getPawns(BitChessBoard.nBlack);
        long whitePassers = BB.wFrontFill(whitePawns) & ~BB.bFrontFill(blackPawns) & whitePawns;
        long blackPassers = BB.bFrontFill(blackPawns) & ~BB.wFrontFill(whitePawns) & blackPawns;

        // fill attack info:
        ParameterizedEvaluation eval = new ParameterizedEvaluation();
        EvalResult e = new EvalResult();
        eval.getMobEvaluation().eval(e, board);

        int result = ppe.calculateScores(board, e, whitePassers, blackPassers);
        return result;
    }

}