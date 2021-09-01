package org.mattlang.jc.engine.evaluation.pstEval2;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.evaluation.taperedEval.EndGameMaterialEval;
import org.mattlang.jc.engine.evaluation.taperedEval.EvalStats;
import org.mattlang.jc.engine.evaluation.taperedEval.KingSafetyEval;
import org.mattlang.jc.engine.evaluation.taperedEval.PawnStructureEval;

/**
 * Another experimental evaluation.
 * It uses basically the minimal pst evaluation and
 * additionally some positional evaluations like
 *
 * - EndgameMaterial
 * - PawnStructure
 * - KingSafety
 */
public class PstEvaluation2 implements EvaluateFunction {

    private MinimalPstEvaluation pstEvaluation = new MinimalPstEvaluation();

    private EndGameMaterialEval endGameMaterialEval = new EndGameMaterialEval();

    private PawnStructureEval pawnStructureEval = new PawnStructureEval();

    private KingSafetyEval kingSafetyEval = new KingSafetyEval();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        int score = pstEvaluation.eval(currBoard, who2Move);

        EvalStats evalStats = new EvalStats(currBoard);

        //        score += pawnStructureEval.eval(evalStats, who2Move);

//        score += endGameMaterialEval.eval(evalStats, who2Move);

        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        score +=
                // two bishop bonus:
                TWO_BISHOP_BONUS * (wp.getBishops().size() == 2 ? 1 : 0 - bp.getBishops().size() == 2 ? 1 : 0) * who2mov
                        +
                        // penalty for two knights:
                        TWO_ROOKS_PENALTY * (wp.getKnights().size() == 2 ? 1 : 0 - bp.getKnights().size() == 2 ? 1 : 0)
                                * who2mov
                        +
                        // penalty for having no pawns (especially in endgame)
                        NO_PAWNS_PENALTY * (wp.getPawns().size() == 0 ? 1 : 0 - bp.getPawns().size() == 0 ? 1 : 0)
                                * who2mov;

        //        score += kingSafetyEval.eval(currBoard, evalStats, who2Move);

        return score;
    }
}
