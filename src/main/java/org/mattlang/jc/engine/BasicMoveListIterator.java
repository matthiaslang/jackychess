package org.mattlang.jc.engine;

import java.util.Iterator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;

public class BasicMoveListIterator implements Iterator<MoveCursor> {

    private BasicMoveList movelist;

    private Move currMove;
    private Move undoMove;
    private Iterator<Move> iterator;

    private MoveCursor curr = new MoveCursor() {

        @Override
        public void move(Board board) {
            undoMove = currMove.move(board);
        }

        @Override
        public Move getMove() {
            return currMove;
        }

        @Override
        public void undoMove(Board board) {
            undoMove.move(board);
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
