package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.engine.evaluation.parameval.PassedPawnEval.onlyPawnsOrOneNightOrBishop;

import org.junit.Test;
import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.tools.FenFlip;

public class PassedPawnEvalTest {

    @Test
    public void testUnStoppableIn1Moves() {
        BitBoard board = new BitBoard();
        String fen = "position fen k7/4P3/8/8/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    private BitBoard createInverted(String fen) {
        FenFlip fenflip = new FenFlip();
        String flippedFen = fenflip.mirrorHorizontalFen(fen);
        BitBoard board = new BitBoard();

        board.setFenPosition(flippedFen);

        return board;
    }

    @Test
    public void testUnStoppableIn2Moves() {
        BitBoard board = new BitBoard();
        String fen = "position fen k7/8/4P3/8/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    @Test
    public void testKingInFront() {
        BitBoard board = new BitBoard();
        String fen = "position fen 3k4/8/4P3/8/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    @Test
    public void testAttacksOnPath() {
        BitBoard board = new BitBoard();
        String fen = "position fen k7/8/2b1P3/8/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    @Test
    public void testBishopCantStopItButAttacks() {
        BitBoard board = new BitBoard();
        String fen = "position fen k7/4P3/3b4/8/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    @Test
    public void testBishopCantStopIt() {
        BitBoard board = new BitBoard();
        String fen = "position fen k7/4P3/1b6/8/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    @Test
    public void testWithBishop() {
        BitBoard board = new BitBoard();
        String fen = "position fen k7/8/8/5Pb1/K7/8/8/8 w - - 2 17 ";
        board.setFenPosition(fen);
        System.out.println(board.toUniCodeStr());

        int result = calcPassedPawnEval(board);
        System.out.println(result);

        int invResult = calcPassedPawnEval(createInverted(fen));
        assertThat(invResult).isEqualTo(-result);
    }

    @Test
    public void testOnlyPawnOrBishopOrKnight() {
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpP"), nWhite)).isTrue();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpP"), nBlack)).isTrue();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBb"), nWhite)).isTrue();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBb"), nBlack)).isTrue();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNn"), nWhite)).isTrue();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNn"), nBlack)).isTrue();

        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNNnn"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNNnn"), nBlack)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBBbb"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBBbb"), nBlack)).isFalse();

        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBNbn"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBNbn"), nBlack)).isFalse();

        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPRr"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPRr"), nBlack)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBbRr"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBbRr"), nBlack)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNnRr"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNnRr"), nBlack)).isFalse();

        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPQq"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPQq"), nBlack)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBbQq"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPBbQq"), nBlack)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNnQq"), nWhite)).isFalse();
        assertThat(onlyPawnsOrOneNightOrBishop(new Material("KkpPNnQq"), nBlack)).isFalse();
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

        long whitePawns = board.getBoard().getPawns(nWhite);
        long blackPawns = board.getBoard().getPawns(nBlack);
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