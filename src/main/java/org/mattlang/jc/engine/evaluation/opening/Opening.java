package org.mattlang.jc.engine.evaluation.opening;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;

public class Opening {

    /**
     * Bishop pattern with bonus to develop to center places and penalty for start position
     */
    private static byte[] BISHOP_PATTERN = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 2, 3, 3, 3, 3, 2, 0,
            0, 2, 5, 5, 5, 5, 2, 0,
            0, 0, 3, 5, 5, 3, 0, 0,
            0, 0, 0, 2, 2, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    };

    /**
     * Knight pattern with bonus to develop to center places and penalty for start position
     */
    private static byte[] KNIGHT_PATTERN = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 3, 3, 3, 3, 1, 0,
            0, 2, 3, 5, 5, 3, 2, 0,
            0, 0, 3, 5, 5, 3, 0, 0,
            0, 0, 0, 2, 2, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    };

    /**
     * Queen pattern with penalty to not develop to early...
     */
    private static byte[] QUEEN_PATTERN = {
            -30, -30, -30, -30, -30, -30, -30, -30,
            -30, -30, -30, -30, -30, -30, -30, -30,
            -30, -30, -30, -30, -30, -30, -30, -30,
            -30, -30, -20, -20, -20, -20, -30, -30,
            -30, -30, -20, -10, -10, -20, -30, -30,
            -30, -30, -20, -10, -10, -20, -30, -30,
            -30, -30, -20, -20, -20, -20, -30, -30,
            0, 0, 0, 0, 0, 0, 0, 0,
    };

    public static final int openingPatterns(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int score = 0;

        int w = 0;
        int b = 0;
        for (int bishop : wp.getBishops().getArr()) {
            w += BISHOP_PATTERN[63 - bishop];
        }
        for (int bishop : bp.getBishops().getArr()) {
            b += BISHOP_PATTERN[bishop];
        }
        score += (w - b) * who2mov;

        w = 0;
        b = 0;
        for (int knight : wp.getKnights().getArr()) {
            w += KNIGHT_PATTERN[63 - knight];
        }
        for (int knight : bp.getKnights().getArr()) {
            b += KNIGHT_PATTERN[knight];
        }
        score += (w - b) * who2mov;

        w = 0;
        b = 0;
        for (int queen : wp.getQueens().getArr()) {
            w += QUEEN_PATTERN[63 - queen];
        }
        for (int queen : bp.getQueens().getArr()) {
            b += QUEEN_PATTERN[queen];
        }
        score += (w - b) * who2mov;

        return score;
    }

    private int calcPatternScore(int[] whiteFigures, int[] blackFigures, byte[] pattern, int who2mov) {
        int w = 0;
        int b = 0;
        for (int bishop : whiteFigures) {
            w += pattern[63 - bishop];
        }
        for (int bishop : blackFigures) {
            b += pattern[bishop];
        }
        return (w - b) * who2mov;
    }
}
