package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;

public class MaterialStats {

    private int counts[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

    public void init(Board board) {
        for (int i = 0; i < 64; i++) {
            Figure figure = board.getFigure(i);
            add(figure);
        }
    }

    public void add(Figure figure) {
        if (figure != Figure.EMPTY) {
            int clr = figure.color == Color.WHITE ? 0 : 1;
            counts[clr][figure.figureType.figureCode] += 1;
        }
    }

    public void clear() {
        counts = new int[][] { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };
    }

    public void remove(Figure figure) {
        if (figure != Figure.EMPTY) {
            int clr = figure.color == Color.WHITE ? 0 : 1;
            counts[clr][figure.figureType.figureCode] -= 1;
            if (counts[clr][figure.figureType.figureCode] < 0) {
                throw new IllegalStateException("that should never be the case!!");
            }
        }
    }

    public int[][] getCounts() {
        return counts;
    }
}
