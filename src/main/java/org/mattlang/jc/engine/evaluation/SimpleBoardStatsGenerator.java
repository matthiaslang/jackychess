package org.mattlang.jc.engine.evaluation;

import java.util.Iterator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.MoveGenerator;

public class SimpleBoardStatsGenerator implements MoveList {

    public long mobility2 = 0L;
    private long captures2 = 0L;

    private MoveGenerator moveGenerator = new MoveGenerator();

    public BoardStats gen(Board board, Color color) {
        mobility2 = 0L;
        captures2 = 0L;
        moveGenerator.generate(board, color, this);
        return new BoardStats(Long.bitCount(mobility2), Long.bitCount(captures2));
    }

    @Override
    public void genMove(int from, int to, Figure capturedFigure) {
        mobility2 |= (1L << to);
        if (capturedFigure != null) {
            captures2 |= (1L << to);
        }
    }

    @Override
    public void genPawnMove(int from, int to, Figure pawn, Figure capturedFigure) {
        mobility2 |= (1L << to);
        if (capturedFigure != null) {
            captures2 |= (1L << to);
        }
    }

    @Override
    public void addRochadeLongWhite() {
        // nothing to do
    }

    @Override
    public void addRochadeShortWhite() {
        // nothing to do
    }

    @Override
    public void addRochadeShortBlack() {
        // nothing to do
    }

    @Override
    public void addRochadeLongBlack() {
        // nothing to do
    }

    @Override
    public void addMove(MoveCursor moveCursor) {
        // nothing to do
    }

    @Override
    public boolean capturesFigure(Figure figure) {
        return false;
    }

    @Override
    public void sortByCapture() {
        // nothing to do
    }

    @Override
    public int size() {
        // nothing to do
        return 0;
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        // nothing to do
        return null;
    }
}
