package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.board.Tools.push_to_edge;
import static org.mattlang.jc.board.Tools.relativeRank;
import static org.mattlang.jc.engine.evaluation.parameval.endgame.EndgameFunction.assertMat;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.material.Material;

/**
 * KNN vs KP. Very drawish, but there are some mate opportunities if we can
 * /// press the weakSide King to a corner before the pawn advances too much.
 */
public class KNNvsKPEndgameFunction implements EndgameFunction {

    public static final Material KNN_KP = new Material("KNNkp");

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        assertMat(board, KNN_KP);

        int weakPawn = Long.numberOfTrailingZeros(bb.getPawns(weaker));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        int result = matEvaluation.getPawnEG()
                + 2 * push_to_edge(weakKing)
                - 10 * relativeRank(weaker, weakPawn);

        return stronger == board.getSiteToMove() ? result : -result;
    }

}
