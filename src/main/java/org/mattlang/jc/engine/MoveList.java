package org.mattlang.jc.engine;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;

public interface MoveList extends Iterable<MoveCursor> {

    void genMove(int from, int to, byte capturedFigureCode);

    void genPawnMove(int from, int to, Color color, byte capturedFigureCode, int enPassantOption);

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
    void sortByCapture();

    int size();

    void genEnPassant(int i, int n, Color side, int enPassantCapturePos);

    /**
     * hook to get "hypothetical" captures of pawns.
     * Means a capture move of a pawn, but to an empty field.
     * This is needed to generate capture statistics and properly recognize patt situations.
     * @param from
     * @param to
     */
    void hypotheticalPawnCapture(int from, int to);
}