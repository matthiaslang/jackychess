package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;

public final class MoveCursorImpl implements MoveCursor {

    private int size;
    private int iterCurser = 0;
    private MoveListImpl movelist;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    public MoveCursorImpl(MoveListImpl movelist) {

        this.movelist = movelist;
        this.iterCurser = -1;
        this.size = movelist.size();
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
        movelist.remove(iterCurser);
        size = movelist.size();
        iterCurser--;
    }

    @Override
    public void next() {

        iterCurser++;
        currMove = movelist.get(iterCurser);
        orderOfCurrentMove= movelist.getOrder(iterCurser);
        currMoveObj.fromLongEncoded(currMove);
    }

    @Override
    public boolean hasNext() {
        return iterCurser + 1 < size;
    }
}
