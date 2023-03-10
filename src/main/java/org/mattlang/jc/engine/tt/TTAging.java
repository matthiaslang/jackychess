package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.board.FigureConstants.FT_PAWN;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitChessBoard;

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
        return board.getBoard().getPieceSet(FT_PAWN);
    }

    private int figureCount(BoardRepresentation board) {
        BitChessBoard bb = board.getBoard();

        return bb.getPawnsCount() +
                bb.getKnightsCount() +
                bb.getBishopsCount() +
                bb.getRooksCount() +
                bb.getQueensCount();

    }

    public void reset() {
        currAging = 1;
        lastBoard = null;
    }
}
