package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.EvaluateFunction;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;
import static org.mattlang.jc.board.FigureType.*;

/**
 * Experimental Material Evaluation.
 *
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class MaterialNegaMaxEval implements EvaluateFunction {

    /* King wheight, aka Matt Wheight. */
    public static final int KING_WHEIGHT = 200000000;

    SimpleBoardStatsGenerator statsgenerator = new SimpleBoardStatsGenerator();

    @Override
    public int eval(Board currBoard, Color who2Move) {
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
        int score = KING_WHEIGHT * (counts[0][King.figureCode] - counts[1][King.figureCode]) * who2mov +
                900 * (counts[0][Queen.figureCode] - counts[1][Queen.figureCode]) * who2mov +
                500 * (counts[0][Rook.figureCode] - counts[1][Rook.figureCode]) * who2mov +
                330 * (counts[0][Bishop.figureCode] - counts[1][Bishop.figureCode]) * who2mov +
                320 * (counts[0][Knight.figureCode] - counts[1][Knight.figureCode]) * who2mov +
                100 * (counts[0][Pawn.figureCode] - counts[1][Pawn.figureCode]) * who2mov +
                // two bishop bonus:
                50 * (counts[0][Bishop.figureCode] == 2 ? 1 : 0 - counts[1][Bishop.figureCode] == 2 ? 1 : 0) * who2mov +
                // penalty for two knights:
                -50 * (counts[0][Knight.figureCode] == 2 ? 1 : 0 - counts[1][Knight.figureCode] == 2 ? 1 : 0) * who2mov
                +
                // penalty for having no pawns (especially in endgame)
                -500 * (counts[0][Pawn.figureCode] == 0 ? 1 : 0 - counts[1][Pawn.figureCode] == 0 ? 1 : 0) * who2mov;


        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);
        score += 10 * (wstats.mobility - bstats.mobility) * who2mov +
                20 * (wstats.captures - bstats.captures) * who2mov;

        score += -100000 * (isPatt(wstats, bstats) - isPatt(bstats, wstats)) * who2mov;

        return score;

    }

    private int isPatt(BoardStats me, BoardStats other) {
        // if only king of other side can move, we need to take care and further analyse:
        if (Long.bitCount(other.kingMobilityBitBoard) == other.mobility) {
            // diff our mobility with the enemies king mobility:
            if ((me.mobilityBitBoard & other.kingMobilityBitBoard) == other.kingMobilityBitBoard) {
                // big penalty for patt!
                // todo or make additional evaluations based on material if we should do accept the patt?
                return 1;
            }
        }
        return 0;
    }
}
