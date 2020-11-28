package org.mattlang.jc.engine;

import org.mattlang.jc.board.Figure;

public interface MoveList extends Iterable<MoveCursor> {

    void genMove(int from, int to, Figure capturedFigure);

    void genPawnMove(int from, int to, Figure pawn, Figure capturedFigure);

    void addRochadeLongWhite();

    void addRochadeShortWhite();

    void addRochadeShortBlack();

    void addRochadeLongBlack();

    int size();
}