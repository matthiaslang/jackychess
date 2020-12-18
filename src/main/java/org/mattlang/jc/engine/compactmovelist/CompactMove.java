package org.mattlang.jc.engine.compactmovelist;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

import static org.mattlang.jc.board.IndexConversion.convert;

public class CompactMove implements Move {

    private byte[] move;
    private byte capturedFigure = (byte)0;
    private boolean undoing;

    public CompactMove(byte[] move) {
        this.move = move;
        if (move[CompactMoveList.IDX_CAPTURE] != 0) {
            capturedFigure = move[CompactMoveList.IDX_CAPTURE];
        }
    }

    public CompactMove(byte[] move, boolean undoing) {
        this.move = move;
        this.undoing = undoing;
    }

    @Override
    public Move move(BoardRepresentation board) {
        if (undoing) {
            CompactMoveList.undoMove(board, move);
            return new CompactMove(move);
        } else {
            CompactMoveList.move(board, move);
            // create also an undo move to fullfill interface:
            return new CompactMove(move, true);
        }
    }

    @Override
    public byte getCapturedFigure() {
        return capturedFigure;
    }

    @Override
    public String toStr() {
        return convert(move[CompactMoveList.IDX_FROM]) + convert(move[CompactMoveList.IDX_TO]);
    }
}
