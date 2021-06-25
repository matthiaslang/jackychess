package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public final class MoveCursorImpl implements MoveCursor {

    private byte castlingrightsBefore;
    private int size;
    private int iterCurser = 0;
    private MoveListImpl movelist;

    private long currMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    public MoveCursorImpl(MoveListImpl movelist) {

        this.movelist = movelist;
        this.iterCurser = -1;
        this.size = movelist.size();
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
    public void remove() {
        movelist.remove(iterCurser);
        size = movelist.size();
        iterCurser--;
    }

    public void next() {

        iterCurser++;
        currMove = movelist.get(iterCurser);
        currMoveObj.fromLongEncoded(currMove);
    }

    public boolean hasNext() {
        return iterCurser + 1 < size;
    }
}