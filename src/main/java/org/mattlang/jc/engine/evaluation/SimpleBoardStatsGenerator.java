package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.movegenerator.MoveCollector;
import org.mattlang.jc.movegenerator.MoveGenerator;

public class SimpleBoardStatsGenerator implements MoveCollector, BoardStatsGenerator {

    public long mobility = 0L;
    private long captures = 0L;

    private long kingMobility = 0L;

    private long hypotheticalPawnCaptures = 0L;

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
        return new BoardStats(mobility, captures, kingMobility, hypotheticalPawnCaptures);
    }

    @Override
    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
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
    public void genPawnMove(int from, int to, Color side, byte capturedFigure, int enPassantOption) {
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
        /**
         * we count hypothetical pawn captures.
         * This is to 1. may let this influence to the eval function
         * 2. to properly recognize patt situations.
         */
        hypotheticalPawnCaptures |= (1L << to);
    }

}
