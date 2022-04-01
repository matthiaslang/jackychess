package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.Tools.push_to_edge;
import static org.mattlang.jc.engine.evaluation.Tools.relativeRank;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;

/**
 * KNN vs KP. Very drawish, but there are some mate opportunities if we can
 * /// press the weakSide King to a corner before the pawn advances too much.
 */
public class KNNvsKPEndgameFunction implements EndgameFunction {

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        //        assert (verify_material(pos, strongSide, 2 * KnightValueMg, 0));
        //        assert (verify_material(pos, weakSide, VALUE_ZERO, 1));

        int weakPawn = Long.numberOfTrailingZeros(bb.getPawns(weaker));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        int result = matEvaluation.getPawnEG()
                + 2 * push_to_edge(weakKing)
                - 10 * relativeRank(weaker, weakPawn);

        return stronger == board.getSiteToMove() ? result : -result;
    }
}
