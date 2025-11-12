package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.board.Tools.push_to_edge;
import static org.mattlang.jc.engine.evaluation.parameval.endgame.EndgameFunction.assertMat;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.material.Material;

/**
 * KR vs KB. This is very simple, and always returns drawish scores. The
 * score is slightly bigger when the defending king is close to the edge.
 */
public class KRvsKBEndgameFunction implements EndgameFunction {

    private static final Material KR_KB = new Material("KRkb");

    @Override
    public int evaluate(BoardRepresentation board, int stronger, int weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        assertMat(board, KR_KB);

        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        int result = push_to_edge(weakKing);

        return stronger == board.getSiteToMove().ordinal() ? result : -result;

    }
}
