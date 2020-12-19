package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

import java.util.Iterator;

public class BasicMoveListIterator implements Iterator<MoveCursor> {

    private BasicMoveList movelist;

    private Move currMove;
    private Move undoMove;
    private Iterator<Move> iterator;

    private MoveCursor curr = new MoveCursor() {

        @Override
        public void move(BoardRepresentation board) {
            undoMove = currMove.move(board);
        }

        @Override
        public Move getMove() {
            return currMove;
        }

        @Override
        public void undoMove(BoardRepresentation board) {
            undoMove.move(board);
        }

        @Override
        public boolean isCapture() {
            return currMove.getCapturedFigure() != 0;
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
