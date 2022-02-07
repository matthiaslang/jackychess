package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.LongSorter;

public final class LazySortedMoveCursorImpl implements MoveCursor {

    private LongSorter longSorter;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    public LazySortedMoveCursorImpl(LongSorter longSorter) {
        this.longSorter = longSorter;
    }

    @Override
    public void move(BoardRepresentation board) {
       board.domove(currMoveObj);
    }

    @Override
    public int getMoveInt() {
        return currMove;
    }

    @Override
    public int getOrder() {
        return orderOfCurrentMove;
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        board.undo(currMoveObj);
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
    public Figure getPromotedFigure() {
        return currMoveObj.getPromotedFigure();
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
        orderOfCurrentMove = longSorter.getOrder();
        currMoveObj.fromLongEncoded(currMove);
    }

    public boolean hasNext() {
        return longSorter.hasNext();
    }
}
