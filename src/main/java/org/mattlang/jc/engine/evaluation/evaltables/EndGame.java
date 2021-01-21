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

    /**
     * The King is better to have not on the border in endgame and more powerful in the middle of the board,
     * or directed to the oponents end to support pawn promotion.
     */
    private final static Pattern KING_PATTERN = new Pattern(new byte[]{
            -20, -20, -20, -20, -20, -20, -20, -20,
            -20, 20, 20, 20, 20, 20, 20,-20,
            -20, 20, 30, 30, 30, 30, 20,-20,
            -20, 10, 30, 30, 30, 30, 10,-20,
            -20, 10, 30, 30, 30, 30, 10,-20,
            -20, 10, 30, 30, 30, 30, 10,-20,
            -20, 10, 10, 10, 10, 10, 10,-20,
            -20, -20, -20, -20, -20, -20, -20, -20,
    });



    public static final int endgamePatterns(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int score = 0;

        score += PAWN_PATTERN.calcScore(wp.getPawns(), bp.getPawns(), who2mov);
        score += KING_PATTERN.calcScore(wp.getKing(), bp.getKing(), who2mov);

        return score;
    }

}
