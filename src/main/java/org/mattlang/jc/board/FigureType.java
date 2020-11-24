package org.mattlang.jc.board;

public enum FigureType {

    EMPTY((byte) 16, ' '),
    Pawn((byte) 1, 'P'),
    Knight((byte) 2, 'N'),
    Bishop((byte) 3, 'B'),
    Rook((byte) 4, 'R'),
    Queen((byte) 5, 'Q'),
    King((byte) 6, 'K');

    public final byte figureCode;
    public final char figureChar;

    FigureType(byte figureCode, char figureChar) {
        this.figureCode = figureCode;
        this.figureChar = figureChar;
    }

}
