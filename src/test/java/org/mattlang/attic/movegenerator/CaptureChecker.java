package org.mattlang.attic.movegenerator;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.movegenerator.MoveCollector;
import org.mattlang.jc.moves.CastlingMove;

public final class CaptureChecker implements MoveCollector {

    private long capturedFigures = 0L;

    @Override
    public void genMove(byte figureType, int from, int to, byte capturedFigureCode) {
        addCapture(capturedFigureCode);
    }

    @Override
    public void genPawnMove(int from, int to, Color color, byte capturedFigureCode) {
        addCapture(capturedFigureCode);
    }

    @Override
    public void addCastlingMove(CastlingMove castlingMove) {
        
    }

    private void addCapture(byte capturedFigureCode) {
        if (capturedFigureCode != 0) {
            byte capturedFigure = (byte) (capturedFigureCode & MASK_OUT_COLOR);
            capturedFigures |= (1L << capturedFigure);
        }
    }


    @Override
    public void genEnPassant(int i, int n, Color side, int enPassantCapturePos) {

    }

    public boolean hasCapturesBy(byte figureCode) {
        return (capturedFigures & (1L << figureCode)) > 0;
    }

    public void reset() {
        capturedFigures = 0L;
    }
}
