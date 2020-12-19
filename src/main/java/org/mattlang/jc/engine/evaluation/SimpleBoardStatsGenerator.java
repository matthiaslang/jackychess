package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.MoveGenerator;

import java.util.Iterator;

public class SimpleBoardStatsGenerator implements MoveList, BoardStatsGenerator {

    public long mobility = 0L;
    private long captures = 0L;

    private long kingMobility = 0L;

    private int kingPos = 0;

    private MoveGenerator moveGenerator = Factory.getDefaults().moveGenerator.create();

    @Override
    public BoardStats gen(BoardRepresentation board, Color color) {
        // find king on board:
        byte kingFigureCode = (byte) (FigureType.King.figureCode | color.code);
        kingPos = board.findPosOfFigure(kingFigureCode);

        mobility = 0L;
        captures = 0L;
        kingMobility = 0L;
        moveGenerator.generate(board, color, this);
        return new BoardStats(mobility, captures, kingMobility);
    }

    @Override
    public void genMove(int from, int to, byte capturedFigure) {
        countMove(from, to, capturedFigure);
    }

    private void countMove(int from, int to, byte capturedFigure) {
        mobility |= (1L << to);
        if (capturedFigure != 0) {
            captures |= (1L << to);
        }

        if (from == kingPos) {
            kingMobility |= (1L << to);
        }
    }

    @Override
    public void genPawnMove(int from, int to, Color side, byte capturedFigure) {
        countMove(from, to, capturedFigure);
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
