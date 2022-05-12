package org.mattlang.jc.board.bitboard;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.IncrementalEvaluateFunction;

public class EmptyIncrementalEvaluateFunction implements IncrementalEvaluateFunction {

    @Override
    public void initIncrementalValues(BoardRepresentation board) {

    }

    @Override
    public void removeFigure(int pos, byte oldFigure) {

    }

    @Override
    public void addFigure(int pos, byte figureCode) {

    }

    @Override
    public void moveFigure(int from, int to, byte figCode) {

    }

    @Override
    public void unregisterIncrementalEval() {

    }
}
