package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.LongSorter;

public final class LazySortedMoveCursorImpl implements MoveCursor {

    private byte castlingrightsBefore;

    private LongSorter longSorter;

    private long currMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    public LazySortedMoveCursorImpl(LongSorter longSorter) {
        this.longSorter = longSorter;
    }

    @Override
    public void move(BoardRepresentation board) {
        castlingrightsBefore = board.getCastlingRights();
        currMoveObj.move(board);
    }

    @Override
    public Move getMove() {
        // todo we should remove this method completely if possible and only deliver the long value ...
        return new MoveImpl(currMove);
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        currMoveObj.undo(board);
        if (castlingrightsBefore != -1) {
            board.setCastlingRights(castlingrightsBefore);
        }
    }

    @Override
    public boolean isCapture() {
        return currMoveObj.isCapture();
    }

    @Override
    public boolean isPawnPromotion() {
        return currMoveObj.isPromotion();
    }

    @Override
    public boolean isEnPassant() {
        return currMoveObj.isEnPassant();
    }

    @Override
    public boolean isCastling() {
        return currMoveObj.isCastling();
    }

    @Override
    public byte getCapturedFigure() {
        return currMoveObj.getCapturedFigure();
    }

    @Override
    public byte getFigureType() {
        return currMoveObj.getFigureType();
    }

    @Override
    public int getFromIndex() {
        return currMoveObj.getFromIndex();
    }

    @Override
    public int getToIndex() {
        return currMoveObj.getToIndex();
    }

    @Override
    public void remove() {
        throw new IllegalStateException("not allowed in a lazy sorted move cursor impl!");
    }

    public void next() {
        currMove = longSorter.next();
        currMoveObj.fromLongEncoded(currMove);
    }

    public boolean hasNext() {
        return longSorter.hasNext();
    }
}
