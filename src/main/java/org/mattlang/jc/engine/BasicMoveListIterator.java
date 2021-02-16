package org.mattlang.jc.engine;

import java.util.Iterator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.PawnPromotionMove;

public class BasicMoveListIterator implements Iterator<MoveCursor> {

    private BasicMoveList movelist;

    private Move currMove;
    private Iterator<Move> iterator;

    private byte castlingrightsBefore;

    private MoveCursor curr = new MoveCursor() {

        @Override
        public void move(BoardRepresentation board) {
            castlingrightsBefore = board.getCastlingRights();
            currMove.move(board);
        }

        @Override
        public Move getMove() {
            return currMove;
        }

        @Override
        public void undoMove(BoardRepresentation board) {
            currMove.undo(board);
            if (castlingrightsBefore != -1) {
                board.setCastlingRights(castlingrightsBefore);
            }
        }

        @Override
        public boolean isCapture() {
            return currMove.getCapturedFigure() != 0;
        }

        @Override
        public boolean isPawnPromotion() {
            return currMove instanceof PawnPromotionMove;
        }
    };

    public BasicMoveListIterator(BasicMoveList movelist) {
        this.movelist = movelist;
        this.iterator = movelist.getMoves().iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public MoveCursor next() {
        currMove = iterator.next();
        return curr;
    }
}
