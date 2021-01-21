package org.mattlang.jc.engine.evaluation.evaltables;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;

public class Opening {

    /**
     * Bishop pattern with bonus to develop to center places and penalty for start position
     */
    private final static Pattern BISHOP_PATTERN = new Pattern(new byte[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 2, 3, 3, 3, 3, 2, 0,
            0, 2, 5, 5, 5, 5, 2, 0,
            0, 0, 3, 5, 5, 3, 0, 0,
            0, 0, 0, 2, 2, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    });

    /**
     * Knight pattern with bonus to develop to center places and penalty for start position
     */
    private final static Pattern KNIGHT_PATTERN = new Pattern(new byte[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 3, 3, 3, 3, 1, 0,
            0, 2, 3, 5, 5, 3, 2, 0,
            0, 0, 3, 5, 5, 3, 0, 0,
            0, 0, 0, 2, 2, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    });

    /**
     * Queen pattern with penalty to not develop to early...
     */
    private final static Pattern QUEEN_PATTERN = new Pattern(new byte[] {
            -120, -120, -120, -120, -120, -120, -120, -120,
            -120, -120, -120, -120, -120, -120, -120, -120,
            -120, -120, -120, -120, -120, -120, -120, -120,
            -120, -120, -60, -60, -60, -60, -120, -120,
            -120, -120, -60, -60, -60, -60, -120, -120,
            -120, -120, -60, -60, -60, -60, -120, -120,
            -120, -120, -60, -60, -60, -60, -120, -120,
            0, 0, 0, 0, 0, 0, 0, 0,
    });

    public static final int openingPatterns(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int score = 0;

        score += BISHOP_PATTERN.calcScore(wp.getBishops(), bp.getBishops(), who2mov);
        score += KNIGHT_PATTERN.calcScore(wp.getKnights(), bp.getKnights(), who2mov);
        score += QUEEN_PATTERN.calcScore(wp.getQueens(), bp.getQueens(), who2mov);

        return score;
    }

}
