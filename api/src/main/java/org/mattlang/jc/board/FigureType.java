package org.mattlang.jc.board;

import static org.mattlang.jc.board.FigureConstants.*;

public enum FigureType {

    EMPTY(FT_EMPTY, ' '),
    Pawn(FT_PAWN, 'P'),
    Knight(FT_KNIGHT, 'N'),
    Bishop(FT_BISHOP, 'B'),
    Rook(FT_ROOK, 'R'),
    Queen(FT_QUEEN, 'Q'),
    King(FT_KING, 'K');

    public final byte figureCode;
    public final char figureChar;

    FigureType(byte figureCode, char figureChar) {
        this.figureCode = figureCode;
        this.figureChar = figureChar;
    }

}
