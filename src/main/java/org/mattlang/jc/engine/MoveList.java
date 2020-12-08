package org.mattlang.jc.engine;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;

public interface MoveList extends Iterable<MoveCursor> {

    void genMove(int from, int to, Figure capturedFigure);

    void genPawnMove(int from, int to, Color color, Figure capturedFigure);

    void addRochadeLongWhite();

    void addRochadeShortWhite();

    void addRochadeShortBlack();

    void addRochadeLongBlack();

    /**
     * Add move from another move cursor. This is used for filtering move lists.
     * @param moveCursor
     */
    void addMove(MoveCursor moveCursor);

    /**
     * Does any of the moves capture the following figure? Especially für king capture/legal moves filtering
     * necessary.
     * @param figure
     * @return
     */
    boolean capturesFigure(Figure figure);

    /**
     * First simple try to order moves for alpha beta cut off relevace: "best" guessed moves should be
     *      * processed first to early cut off. First try: sort by capture first moves:
     */
    public void sortByCapture();

    int size();
}