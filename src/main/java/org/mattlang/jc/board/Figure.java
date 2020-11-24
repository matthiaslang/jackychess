package org.mattlang.jc.board;

public enum Figure {

    EMPTY((byte) 16, ' '),
    Pawn((byte) 1, 'P'),
    Knight((byte) 2, 'N'),
    Bishop((byte) 3, 'B'),
    Rook((byte) 4, 'R'),
    Queen((byte) 5, 'Q'),
    King((byte) 6, 'K');

    public static final byte BLACK = 64;

    public final byte figureCode;
    public final char figureChar;

    Figure(byte figureCode, char figureChar) {
        this.figureCode = figureCode;
        this.figureChar = figureChar;
    }

    public static char toFigureChar(byte figure) {
        boolean black = figure >= BLACK;
        char figureChar = ' ';
        if (black) {
            figure = (byte) (0b111111 & figure);
        }
        // todo use map...
        for (Figure fig : Figure.values()) {
            if (fig.figureCode == figure) {
                figureChar = fig.figureChar;
            }
        }
        if (black) {
            figureChar = Character.toLowerCase(figureChar);
        }
        return figureChar;
    }

    public static byte convertFigureChar(char figureChar) {
        byte figure = 0;
        char upperFigChar = Character.toUpperCase(figureChar);
        // todo use map...
        for (Figure fig : Figure.values()) {
            if (fig.figureChar == upperFigChar) {
                figure = fig.figureCode;
            }
        }
        if (Character.isLowerCase(figureChar)) {
            figure |= BLACK;
        }
        return figure;
    }

}
