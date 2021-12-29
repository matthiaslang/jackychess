package org.mattlang.attic.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.movegenerator.MoveCollector;
import org.mattlang.jc.movegenerator.MoveGenerator;

/**
 * Helper class to generate a mobility bitmask on an empty board for usage in check tests, etc.
 */
public class BitmaskProducer implements MoveCollector {

    private MoveGenerator moveGenerator = Factory.getDefaults().moveGenerator.instance();

    private BoardRepresentation board = Factory.getDefaults().boards.create();

    private long mask = 0L;


    public BitmaskProducer() {
        for(int i=0; i<64; i++){
            board.setPos(i, FigureConstants.FT_EMPTY);
        }
    }

    public long genMobilityBitMask(int pos, byte figureType) {

        mask = 0L;
        board.setPos(pos, (byte) (figureType | Color.WHITE.code));
        moveGenerator.generate(board, Color.WHITE, this);
        board.setPos(pos, FigureConstants.FT_EMPTY);

        return mask;
    }

    @Override
    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        countMove(from, to, capturedFigure);
    }

    private void countMove(int from, int to, byte capturedFigure) {
        mask |= (1L << to);
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
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        countMove(from, to, side == Color.WHITE ? FigureConstants.B_PAWN : FigureConstants.W_PAWN);
    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // nothing to do
    }
}
