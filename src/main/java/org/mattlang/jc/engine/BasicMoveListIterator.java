package org.mattlang.jc.engine;

import java.util.Iterator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

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
            return currMove.isCapture();
        }

        @Override
        public boolean isPawnPromotion() {
            return currMove.isPromotion();
        }

        @Override
        public boolean isEnPassant() {
            return currMove.isEnPassant();
        }

        @Override
        public boolean isCastling() {
            return currMove.isCastling();
        }

        @Override
        public void remove() {
             iterator.remove();
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

    @Override
    public void remove() {
        iterator.remove();
    }
}
