package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.board.Rank.RANK_7;
import static org.mattlang.jc.board.Tools.*;
import static org.mattlang.jc.engine.evaluation.parameval.endgame.EndgameFunction.assertMat;

import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.material.Material;

/**
 * KQ vs KP. In general, this is a win for the stronger side, but there are a
 * /// few important exceptions. A pawn on 7th rank and on the A,C,F or H files
 * /// with a king positioned next to it can be a draw, so in that case, we only
 * /// use the distance between the kings.
 */
public class KQvsKPEndgameFunction implements EndgameFunction {

    private static final Material KQ_KP = new Material("KQkp");

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        assertMat(board, KQ_KP);

        int strongKing = Long.numberOfTrailingZeros(bb.getKings(stronger));
        int weakPawn = Long.numberOfTrailingZeros(bb.getPawns(weaker));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        long weakPawnBB = 1L << weakPawn;

        int result = push_close(strongKing, weakKing);

        if (relativeRank(weaker, weakPawn) != RANK_7.ordinal()
                || distance(weakKing, weakPawn) != 1
                || ((BB.B | BB.D | BB.E | BB.G) & weakPawnBB) != 0)
            result += matEvaluation.getQueenEG() - matEvaluation.getPawnEG();

        return stronger == board.getSiteToMove() ? result : -result;
    }
}
