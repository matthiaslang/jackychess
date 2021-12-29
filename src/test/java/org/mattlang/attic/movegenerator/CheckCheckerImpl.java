package org.mattlang.attic.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.CheckChecker;

public class CheckCheckerImpl implements CheckChecker {

    MoveGeneratorImpl3 moveGeneratorImpl2 = new MoveGeneratorImpl3();
    
    @Override
    public boolean isInChess(BoardRepresentation board, Color otherColor) {
        PieceList otherPieces = otherColor == Color.WHITE ? board.getWhitePieces() : board.getBlackPieces();
        int kingPos = otherPieces.getKing();
        return moveGeneratorImpl2.canFigureCaptured(board, kingPos);
    }
}
