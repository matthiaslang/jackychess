package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.FigureType.*;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.EvaluateFunction;

public class SimpleNegaMaxEval implements EvaluateFunction {

    @Override
    public int eval(Board currBoard, Color who2Move) {
        int counts[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

        for (int i = 0; i < 64; i++) {
            Figure figure = currBoard.getFigure(i);
            if (figure != Figure.EMPTY) {
                int clr = figure.color == Color.WHITE ? 0 : 1;
                counts[clr][figure.figureType.figureCode] += 1;
            }
        }
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        int score = 2000000 * (counts[0][King.figureCode] - counts[1][King.figureCode]) * who2mov +
                9 * (counts[0][Queen.figureCode] - counts[1][Queen.figureCode]) * who2mov +
                5 * (counts[0][Rook.figureCode] - counts[1][Rook.figureCode]) * who2mov +
                3 * (counts[0][Bishop.figureCode] - counts[1][Bishop.figureCode]) * who2mov +
                3 * (counts[0][Knight.figureCode] - counts[1][Knight.figureCode]) * who2mov +
                1 * (counts[0][Pawn.figureCode] - counts[1][Pawn.figureCode]) * who2mov;

        return score;

    }
}
