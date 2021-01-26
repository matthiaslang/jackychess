package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.PAWN_WEIGHT;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.evaltables.EndGame;

/**
 * Experimental Material Evaluation.
 * <p>
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class EndgameEval {

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {

        // apply opening patterns:
        int score = EndGame.endgamePatterns(currBoard, who2Move);

        /**
         * Special end game material evaluations:
         */
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();
        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        // pawns are more worth in endgame: lets say thery are 1 1/2 of pawns normally:
        score += PAWN_WEIGHT / 2 * (wp.getPawns().size() - bp.getPawns().size()) * who2mov;

        return score;

    }



}
