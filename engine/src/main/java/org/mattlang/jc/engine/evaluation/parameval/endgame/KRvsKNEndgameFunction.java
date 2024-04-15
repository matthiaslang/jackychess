package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.Tools.push_away;
import static org.mattlang.jc.engine.evaluation.Tools.push_to_edge;
import static org.mattlang.jc.engine.evaluation.parameval.endgame.EndgameFunction.assertMat;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.material.Material;

/**
 * KR vs KN. The attacking side has slightly better winning chances than
 * in KR vs KB, particularly if the king and the knight are far apart.
 */
public class KRvsKNEndgameFunction implements EndgameFunction {

    private static final Material KR_KN = new Material("KRkn");

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        assertMat(board, KR_KN);

        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));
        int weakKnight = Long.numberOfTrailingZeros(bb.getKnights(weaker));
        int result = push_to_edge(weakKing) + push_away(weakKing, weakKnight);
        return stronger == board.getSiteToMove() ? result : -result;

    }
}
