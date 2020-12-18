package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.EvaluateFunction;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;
import static org.mattlang.jc.board.FigureType.*;
import static org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval.KING_WHEIGHT;

/**
 * Simple Material Evaluation.
 *
 */
public class SimpleNegaMaxEval implements EvaluateFunction {

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
        int score = KING_WHEIGHT * (counts[0][King.figureCode] - counts[1][King.figureCode]) * who2mov +
                900 * (counts[0][Queen.figureCode] - counts[1][Queen.figureCode]) * who2mov +
                500 * (counts[0][Rook.figureCode] - counts[1][Rook.figureCode]) * who2mov +
                300 * (counts[0][Bishop.figureCode] - counts[1][Bishop.figureCode]) * who2mov +
                300 * (counts[0][Knight.figureCode] - counts[1][Knight.figureCode]) * who2mov +
                100 * (counts[0][Pawn.figureCode] - counts[1][Pawn.figureCode]) * who2mov;

        return score;

    }
}
