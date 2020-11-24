package org.mattlang.jc.board;

public enum FigureType {

    EMPTY((byte) 16, ' '),
    Pawn((byte) 0, 'P'),
    Knight((byte) 1, 'N'),
    Bishop((byte) 2, 'B'),
    Rook((byte) 3, 'R'),
    Queen((byte) 4, 'Q'),
    King((byte) 5, 'K');

    public final byte figureCode;
    public final char figureChar;

    FigureType(byte figureCode, char figureChar) {
        this.figureCode = figureCode;
        this.figureChar = figureChar;
    }

}
