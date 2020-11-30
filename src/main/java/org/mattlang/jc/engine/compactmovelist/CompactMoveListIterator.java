package org.mattlang.jc.engine.compactmovelist;

import java.util.Iterator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class CompactMoveListIterator implements Iterator<MoveCursor> {

    private final CompactMoveList moveList;

    private int index = -1;

    private MoveCursor curr = new MoveCursor() {

        @Override
        public void move(Board board) {
            moveList.move(board, index);
        }

        @Override
        public Move getMove() {
            return new CompactMove(moveList.movelist[index]);
        }

        @Override
        public void undoMove(Board board) {
            moveList.undoMove(board, index);
        }
    };

    public CompactMoveListIterator(CompactMoveList moveList) {
        this.moveList = moveList;
    }

    @Override
    public boolean hasNext() {
        return index < moveList.size() - 1;
    }

    @Override
    public MoveCursor next() {
        index++;
        return curr;
    }
}
