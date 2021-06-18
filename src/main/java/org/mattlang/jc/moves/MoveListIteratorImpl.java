package org.mattlang.jc.moves;

import static org.mattlang.jc.moves.MoveImpl.PAWN_PROMOTION_MOVE;

import java.util.Iterator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class MoveListIteratorImpl implements Iterator<MoveCursor> {

    private int size;
    private int iterCurser=0;
    private MoveListImpl movelist;

    private long currMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    private byte castlingrightsBefore;

    private MoveCursor curr = new MoveCursor() {

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
            return currMoveObj.getType() == PAWN_PROMOTION_MOVE;
        }

        @Override
        public void remove() {
            movelist.remove(iterCurser);
            size = movelist.size();
            iterCurser--;
        }
    };

    public MoveListIteratorImpl(MoveListImpl movelist) {
        this.movelist = movelist;
        this.iterCurser=-1;
        this.size = movelist.size();
    }

    @Override
    public boolean hasNext() {
        return iterCurser+1 < size;
    }

    @Override
    public MoveCursor next() {
        iterCurser++;
        currMove = movelist.get(iterCurser);
        currMoveObj.fromLongEncoded(currMove);
        return curr;
    }

    @Override
    public void remove() {
        movelist.remove(iterCurser);
        size = movelist.size();
        iterCurser--;
    }
}
