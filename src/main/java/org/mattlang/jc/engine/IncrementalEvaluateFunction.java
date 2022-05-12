package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;

public interface IncrementalEvaluateFunction {

    void initIncrementalValues(BoardRepresentation board);

    void removeFigure(int pos, byte oldFigure);

    void addFigure(int pos, byte figureCode);

    void moveFigure(int from, int to, byte figCode);

    void unregisterIncrementalEval();
}
