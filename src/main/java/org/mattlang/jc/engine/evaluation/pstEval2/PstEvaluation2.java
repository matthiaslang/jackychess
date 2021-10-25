package org.mattlang.jc.engine.evaluation.pstEval2;

import static org.mattlang.jc.engine.evaluation.pstEval2.MobilityEvaluation.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.BoardStatsGenerator;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.evaluation.taperedEval.EndGameMaterialEval;
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

    public static final int TWO_BISHOP_BONUS = 50;

    public static final int TWO_ROOKS_PENALTY = -50;

    public static final int NO_PAWNS_PENALTY = -500;

    private BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    private MinimalPstEvaluation pstEvaluation = new MinimalPstEvaluation();

    private EndGameMaterialEval endGameMaterialEval = new EndGameMaterialEval();

    private PawnStructureEval pawnStructureEval = new PawnStructureEval();

    private KingSafetyEval kingSafetyEval = new KingSafetyEval();

    private MobilityEvaluation mobilityEvaluation = new MobilityEvaluation();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        int score = pstEvaluation.eval(currBoard, who2Move);

        BitBoard bitBoard = (BitBoard) currBoard;

        mobilityEvaluation.eval(bitBoard);
        int[][] results = mobilityEvaluation.results;

        // test code... 

        // mobility (may be not so relevant in this variant, since the pst tables already model mobility indirectly
//        score += (results[IWHITE][MOBILITY] - results[IBLACK][MOBILITY]) * who2mov +
//                (results[IWHITE][CAPTURES] - results[IBLACK][CAPTURES]) * who2mov;


        // therefore maybe we should only model captures and connectivity:
        score += //(results[IWHITE][CONNECTIVITY] - results[IBLACK][CONNECTIVITY]) * who2mov +
                (results[IWHITE][CAPTURES] - results[IBLACK][CAPTURES]) * who2mov;



        //        score +=
        //                // two bishop bonus:
        //                TWO_BISHOP_BONUS * ((wp.getBishops().size() == 2 ? 1 : 0) - (bp.getBishops().size() == 2 ? 1 : 0)) * who2mov;
        //                        +
        //                        // penalty for two knights:
        //                        TWO_ROOKS_PENALTY * ((wp.getKnights().size() == 2 ? 1 : 0) - (bp.getKnights().size() == 2 ? 1 : 0))
        //                                * who2mov
        //                        +
        //                        // penalty for having no pawns (especially in endgame)
        //                        NO_PAWNS_PENALTY * ((wp.getPawns().size() == 0 ? 1 : 0) - (bp.getPawns().size() == 0 ? 1 : 0))
        //                                * who2mov;

        //        score += kingSafetyEval.eval(currBoard, evalStats, who2Move);

        return score;
    }
}
