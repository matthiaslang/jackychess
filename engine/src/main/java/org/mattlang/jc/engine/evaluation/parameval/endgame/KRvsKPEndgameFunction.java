package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.board.Rank.*;
import static org.mattlang.jc.board.Tools.*;
import static org.mattlang.jc.engine.evaluation.parameval.endgame.EndgameFunction.assertMat;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.material.Material;

/**
 * KR vs KP. This is a somewhat tricky endgame to evaluate precisely without
 * a bitbase. The function below returns drawish scores when the pawn is
 * far advanced with support of the king, while the attacking king is far
 * away.
 */
public class KRvsKPEndgameFunction implements EndgameFunction {

    private static final Material KR_KP = new Material("KRkp");

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        assertMat(board, KR_KP);

        int strongKing = Long.numberOfTrailingZeros(bb.getKings(stronger));
        int strongRook = Long.numberOfTrailingZeros(bb.getRooks(stronger));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));
        int weakPawn = Long.numberOfTrailingZeros(bb.getPawns(weaker));
        long weakPawnBB = 1L << weakPawn;

        int queeningSquare = makeSquare(fileOf(weakPawn), relativeRank(weaker, RANK_8));
        int result;

        // If the stronger side's king is in front of the pawn, it's a win
        if ((forward_file_bb(stronger, strongKing) & weakPawnBB) != 0)
            result = matEvaluation.getRookEG() - distance(strongKing, weakPawn);

            // If the weaker side's king is too far from the pawn and the rook,
            // it's a win.
        else if (distance(weakKing, weakPawn) >= 3 + (board.getSiteToMove() == weaker ? 1 : 0)
                && distance(weakKing, strongRook) >= 3)
            result = matEvaluation.getRookEG() - distance(strongKing, weakPawn);

            // If the pawn is far advanced and supported by the defending king,
            // the position is drawish
        else if (relativeRank(stronger, weakKing) <= RANK_3.ordinal()
                && distance(weakKing, weakPawn) == 1
                && relativeRank(stronger, strongKing) >= RANK_4.ordinal()
                && distance(strongKing, weakPawn) > 2 + (board.getSiteToMove() == stronger ? 1 : 0))
            result = 80 - 8 * distance(strongKing, weakPawn);

        else
            result = 200 - 8 * (distance(strongKing, weakPawn + pawn_push(weaker).getOffset())
                    - distance(weakKing, weakPawn + pawn_push(weaker).getOffset())
                    - distance(weakPawn, queeningSquare));

        return stronger == board.getSiteToMove() ? result : -result;

    }
}
