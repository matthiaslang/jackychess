package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.Color;

public final class CaptureChecker implements MoveCollector {

    private long capturedFigures = 0L;

    @Override
    public void genMove(byte figureType, int from, int to, byte capturedFigureCode) {
        addCapture(capturedFigureCode);
    }

    @Override
    public void genPawnMove(int from, int to, Color color, byte capturedFigureCode, int enPassantOption) {
        addCapture(capturedFigureCode);
    }


    private void addCapture(byte capturedFigureCode) {
        if (capturedFigureCode != 0) {
            byte capturedFigure = (byte) (capturedFigureCode & MASK_OUT_COLOR);
            capturedFigures |= (1L << capturedFigure);
        }
    }

    @Override
    public void addRochadeLongWhite() {

    }

    @Override
    public void addRochadeShortWhite() {

    }

    @Override
    public void addRochadeShortBlack() {

    }

    @Override
    public void addRochadeLongBlack() {

    }

    @Override
    public void genEnPassant(int i, int n, Color side, int enPassantCapturePos) {

    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // hypothetical pawn captures are no real captures, therefore we do not count this.
    }

    public boolean hasCapturesBy(byte figureCode) {
        return (capturedFigures & (1L << figureCode)) > 0;
    }

    public void reset() {
        capturedFigures = 0L;
    }
}
