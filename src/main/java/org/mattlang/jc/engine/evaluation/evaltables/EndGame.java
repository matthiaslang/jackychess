package org.mattlang.jc.engine.evaluation.evaltables;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;

public class EndGame {

    /**
     * Pawns in endgame are most interesting for promotion
     */
    private final static Pattern PAWN_PATTERN = new Pattern(new byte[]{
            60, 60, 60, 60, 60, 60, 60, 60,
            50, 50, 50, 50, 50, 50, 50, 50,
            40, 40, 40, 40, 40, 40, 40, 40,
            30, 30, 30, 30, 30, 30, 30, 30,
            20, 20, 20, 20, 20, 20, 20, 20,
            10, 10, 10, 10, 10, 10, 10, 10,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    });


    public static final int endgamePatterns(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int score = 0;

        score += PAWN_PATTERN.calcScore(wp.getPawns(), bp.getPawns(), who2mov);

        return score;
    }

}
