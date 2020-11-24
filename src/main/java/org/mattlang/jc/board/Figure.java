package org.mattlang.jc.board;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

public enum Figure {

    EMPTY(FigureType.EMPTY, WHITE),
    Pawn(FigureType.Pawn, WHITE),
    Knight(FigureType.Knight, WHITE),
    Bishop(FigureType.Bishop, WHITE),
    Rook(FigureType.Rook, WHITE),
    Queen(FigureType.Queen, WHITE),
    King(FigureType.King, WHITE),

    B_Pawn(FigureType.Pawn, BLACK),
    B_Knight(FigureType.Knight, BLACK),
    B_Bishop(FigureType.Bishop, BLACK),
    B_Rook(FigureType.Rook, BLACK),
    B_Queen(FigureType.Queen, BLACK),
    B_King(FigureType.King, BLACK);

    public final byte figureCode;
    public final char figureChar;
    public final FigureType figureType;
    public final Color color;

    Figure(FigureType figureType, Color color) {
        this.figureCode = (byte) (figureType.figureCode | color.code);
        if (color == WHITE) {
            this.figureChar = figureType.figureChar;
        } else {
            this.figureChar = Character.toLowerCase(figureType.figureChar);
        }
        this.figureType = figureType;
        this.color = color;
    }

    public static char toFigureChar(byte figure) {
        return getFigureByCode(figure).figureChar;
    }

    public static byte convertFigureChar(char figureChar) {
        // todo use map...
        for (Figure fig : Figure.values()) {
            if (fig.figureChar == figureChar) {
                return fig.figureCode;
            }
        }
        throw new IllegalStateException("unknown figure char " + figureChar);
    }

    public static Figure getFigureByCode(byte figure) {
        // todo use map...
        for (Figure fig : Figure.values()) {
            if (fig.figureCode == figure) {
                return fig;
            }
        }
        throw new IllegalStateException("unknown figure code.. " + figure);
    }

}
