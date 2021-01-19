package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;
import static org.mattlang.jc.board.FigureType.*;
import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.EvaluateFunction;

/**
 * Experimental Material Evaluation.
 *
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class MaterialNegaMaxEval implements EvaluateFunction {

    BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        int counts[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

        for (int i = 0; i < 64; i++) {
            byte figure = currBoard.getFigureCode(i);
            if (figure != FigureConstants.FT_EMPTY) {
                int clr = (figure & Color.WHITE.code) != 0 ? 0 : 1;
                byte figureCode = (byte) (figure & MASK_OUT_COLOR);
                counts[clr][figureCode] += 1;
            }
        }
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        int score = KING_WEIGHT * (counts[0][King.figureCode] - counts[1][King.figureCode]) * who2mov +
                QUEEN_WEIGHT * (counts[0][Queen.figureCode] - counts[1][Queen.figureCode]) * who2mov +
                ROOK_WEIGHT * (counts[0][Rook.figureCode] - counts[1][Rook.figureCode]) * who2mov +
                BISHOP_WEIGHT * (counts[0][Bishop.figureCode] - counts[1][Bishop.figureCode]) * who2mov +
                KNIGHT_WEIGHT * (counts[0][Knight.figureCode] - counts[1][Knight.figureCode]) * who2mov +
                PAWN_WEIGHT * (counts[0][Pawn.figureCode] - counts[1][Pawn.figureCode]) * who2mov +
                // two bishop bonus:
                TWO_BISHOP_BONUS * (counts[0][Bishop.figureCode] == 2 ? 1 : 0 - counts[1][Bishop.figureCode] == 2 ? 1 : 0) * who2mov +
                // penalty for two knights:
                TWO_ROOKS_PENALTY * (counts[0][Knight.figureCode] == 2 ? 1 : 0 - counts[1][Knight.figureCode] == 2 ? 1 : 0) * who2mov
                +
                // penalty for having no pawns (especially in endgame)
                NO_PAWNS_PENALTY * (counts[0][Pawn.figureCode] == 0 ? 1 : 0 - counts[1][Pawn.figureCode] == 0 ? 1 : 0) * who2mov;


        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);
        score += COMMON_MOBILITY_WEIGHT * (wstats.mobility - bstats.mobility) * who2mov +
                COMMON_CAPTURABILITY_WEIGHT * (wstats.captures - bstats.captures) * who2mov;

        int whiteMakesPatt = isPatt(wstats, bstats);
        int blackMakesPatt = isPatt(bstats, wstats);

        if (whiteMakesPatt == 1 || blackMakesPatt == 1) {
         // patt dominates the score and resets all previous score calculation:
            score = -PATT_WEIGHT * (whiteMakesPatt - blackMakesPatt) * who2mov;
        }

        return score;

    }

    private int isPatt(BoardStats me, BoardStats other) {
        // if only king of other side can move, we need to take care and further analyse:
        if (Long.bitCount(other.kingMobilityBitBoard) == other.mobility) {
            // diff our mobility/captures possibility with the enemies king mobility:
            long possibleCaptures = me.mobilityBitBoard | me.hypotheticalPawnCaptures;
            if ((possibleCaptures & other.kingMobilityBitBoard) == other.kingMobilityBitBoard) {
                // big penalty for patt!
                // todo or make additional evaluations based on material if we should do accept the patt?
                return 1;
            }
        }
        return 0;
    }
}
