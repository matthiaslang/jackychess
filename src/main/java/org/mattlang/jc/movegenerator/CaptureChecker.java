package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

public class CaptureChecker implements MoveList {

    private Set<Byte> capturedFigures = new HashSet<>();


    @Override
    public void genMove(byte castlingRightsBefore, int from, int to, byte capturedFigureCode) {
        addCapture(capturedFigureCode);
    }

    @Override
    public void genPawnMove(int from, int to, Color color, byte capturedFigureCode, int enPassantOption) {
        addCapture(capturedFigureCode);
    }


    private void addCapture(byte capturedFigureCode) {
        if (capturedFigureCode != 0) {
            byte capturedFigure = (byte) (capturedFigureCode & MASK_OUT_COLOR);
            capturedFigures.add(capturedFigure);
        }
    }

    @Override
    public void addRochadeLongWhite(byte castlingRightsBefore) {

    }

    @Override
    public void addRochadeShortWhite(byte castlingRightsBefore) {

    }

    @Override
    public void addRochadeShortBlack(byte castlingRightsBefore) {

    }

    @Override
    public void addRochadeLongBlack(byte castlingRightsBefore) {

    }

    @Override
    public void addMove(MoveCursor moveCursor) {

    }

    @Override
    public boolean capturesFigure(Figure figure) {
        return false;
    }

    @Override
    public void sortByCapture() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void genEnPassant(int i, int n, Color side, int enPassantCapturePos) {

    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // hypothetical pawn captures are no real captures, therefore we do not count this.
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        return null;
    }

    public boolean hasCapturesBy(byte figureCode) {
        return capturedFigures.contains(figureCode);
    }

    public void reset() {
        capturedFigures.clear();
    }
}
