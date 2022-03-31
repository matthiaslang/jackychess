package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.Tools.push_close;
import static org.mattlang.jc.engine.evaluation.Tools.push_to_edge;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;

/**
 * KQ vs KR. This is almost identical to KX vs K: we give the attacking
 * /// king a bonus for having the kings close together, and for forcing the
 * /// defending king towards the edge. If we also take care to avoid null move for
 * /// the defending side in the search, this is usually sufficient to win KQ vs KR.
 */
public class KQvsKREndgameFunction implements EndgameFunction {

    public int evaluate(BoardRepresentation board, int stronger, int weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        int strongKing = Long.numberOfTrailingZeros(bb.getKings(stronger));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        int result = matEvaluation.getQueenEG()
                - matEvaluation.getRookEG()
                + push_to_edge(weakKing)
                + push_close(strongKing, weakKing);

        return stronger == board.getSiteToMove().ordinal() ? result : -result;

    }
}
