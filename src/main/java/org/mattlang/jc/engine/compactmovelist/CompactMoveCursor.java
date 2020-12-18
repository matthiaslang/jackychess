package org.mattlang.jc.engine.compactmovelist;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class CompactMoveCursor implements MoveCursor {

    CompactMoveListIterator iterator;

    public CompactMoveCursor(CompactMoveListIterator iterator) {
        this.iterator = iterator;
    }

    @Override
    public void move(BoardRepresentation board) {
        iterator.moveList.move(board, iterator.index);
    }

    @Override
    public Move getMove() {
        return new CompactMove(iterator.moveList.movelist[iterator.index]);
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        iterator.moveList.undoMove(board, iterator.index);
    }
}
