package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.CheckChecker;

public class CheckCheckerImpl implements CheckChecker {

    MoveGeneratorImpl2 moveGeneratorImpl2 = new MoveGeneratorImpl2();
    
    @Override
    public boolean isInChess(BoardRepresentation board, Color otherColor) {
        PieceList otherPieces = otherColor == Color.WHITE ? board.getWhitePieces() : board.getBlackPieces();
        int kingPos = otherPieces.getKing();
        return moveGeneratorImpl2.canFigureCaptured(board, kingPos);
    }
}
