package org.mattlang.jc.moves;

import java.util.Iterator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class BasicMoveListIterator2 implements Iterator<MoveCursor> {

    private BasicMoveList2 movelist;

    private MoveImpl currMove;
    private Iterator<MoveImpl> iterator;

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
        public byte getCapturedFigure() {
            return currMove.getCapturedFigure();
        }

        @Override
        public byte getFigureType() {
            return currMove.getFigureType();
        }

        @Override
        public int getFromIndex() {
            return currMove.getFromIndex();
        }

        @Override
        public int getToIndex() {
            return currMove.getToIndex();
        }

        @Override
        public void remove() {
             iterator.remove();
        }
    };

    public BasicMoveListIterator2(BasicMoveList2 movelist) {
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
