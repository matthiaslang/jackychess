package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;

public final class LazySortedMoveCursorImpl implements MoveCursor {

    private MoveListImpl moveList;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    public LazySortedMoveCursorImpl(MoveListImpl moveList) {
        this.moveList = moveList;
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
    public int getEnPassantCapturePos() {
        return currMoveObj.getEnPassantCapturePos();
    }

    @Override
    public byte getPromotedFigureByte() {
        return currMoveObj.getPromotedFigureByte();
    }

    @Override
    public CastlingMove getCastlingMove() {
        return currMoveObj.getCastlingMove();
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
    public boolean isPromotion() {
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
    public String toStr() {
        return currMoveObj.toStr();
    }

    private int currIndex = -1;

    public void init(int[] moves, int size, int[] order) {
        currIndex = -1;
    }

    public void next() {
        currIndex++;
        moveList.sort(currIndex);
        currMove = moveList.get(currIndex);
        orderOfCurrentMove = moveList.getOrder(currIndex);
        currMoveObj.fromLongEncoded(currMove);
    }

    public boolean hasNext() {
        return currIndex < moveList.size()-1;
    }
}
