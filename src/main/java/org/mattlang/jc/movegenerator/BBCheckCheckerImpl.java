package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;

public class BBCheckCheckerImpl implements CheckChecker {

    @Override
    public boolean isInChess(BoardRepresentation board, Color otherColor) {
//        PieceList otherPieces = otherColor == Color.WHITE ? board.getWhitePieces() : board.getBlackPieces();
//        int kingPos = otherPieces.getKing();
        return BBMoveGeneratorImpl.canKingCaptured(board, otherColor);
    }
}
