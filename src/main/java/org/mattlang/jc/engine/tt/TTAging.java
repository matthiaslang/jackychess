package org.mattlang.jc.engine.tt;

import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;

/**
 * Keeps track of the aging parameter for tt caches.
 */
public class TTAging {


    /**
     * the current aging value.
     */
    private byte currAging = 1;
    /**
     * the last board representation to check for aging.
     */
    private BoardRepresentation lastBoard;

    public byte updateAging(BoardRepresentation board) {
        if (lastBoard == null) {
            currAging = 1;
        } else {
            if (lastBoard.getCastlingRights() != board.getCastlingRights()
                    || figureCount(lastBoard) != figureCount(board)
                    || differentPawnStructure(lastBoard, board)) {
                currAging++;
                if (currAging > 120) {
                    currAging = 1;
                }
                UCILogger.log("TTCache: updated aging");
            }
        }
        lastBoard = board.copy();

        return currAging;

    }

    private boolean differentPawnStructure(BoardRepresentation board1, BoardRepresentation board2) {
        long pawnMask1 = createPawnMask(board1);
        long pawnMask2 = createPawnMask(board2);
        return pawnMask1 != pawnMask2;

    }

    private long createPawnMask(BoardRepresentation board) {
        long mask = 0L;

        for (int pawn : board.getWhitePieces().getPawns().getArr()) {
            mask |= (1L << pawn);
        }
        for (int pawn : board.getBlackPieces().getPawns().getArr()) {
            mask |= (1L << pawn);
        }

        return mask;
    }

    private int figureCount(BoardRepresentation board) {

        return board.getWhitePieces().getPawns().size()
                + board.getWhitePieces().getKnights().size()
                + board.getWhitePieces().getBishops().size()
                + board.getWhitePieces().getRooks().size()
                + board.getWhitePieces().getQueens().size()
                + board.getBlackPieces().getPawns().size()
                + board.getBlackPieces().getKnights().size()
                + board.getBlackPieces().getBishops().size()
                + board.getBlackPieces().getRooks().size()
                + board.getBlackPieces().getQueens().size();

    }
}
