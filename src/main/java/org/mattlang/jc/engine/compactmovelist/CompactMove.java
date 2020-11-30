package org.mattlang.jc.engine.compactmovelist;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;

public class CompactMove implements Move {

    private byte[] move;
    private Figure capturedFigure = null;

    public CompactMove(byte[] move) {
        this.move = move;
        if (move[CompactMoveList.IDX_CAPTURE] != 0) {
            capturedFigure = Figure.getFigureByCode(move[CompactMoveList.IDX_CAPTURE]);
        }
    }

    @Override
    public Move move(Board board) {
        CompactMoveList.move(board, move);
        // undoes not support and not necessary at that level...
        return null;
    }

    @Override
    public Figure getCapturedFigure() {
        return capturedFigure;
    }

    @Override
    public String toStr() {
        return null;
    }
}
