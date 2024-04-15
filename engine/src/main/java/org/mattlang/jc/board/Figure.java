package org.mattlang.jc.board;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.MAX_FIGURE_INDEX;

public enum Figure {

    EMPTY(FigureType.EMPTY, WHITE, '\u2001'),
    W_Pawn(FigureType.Pawn, WHITE, '♙'),
    W_Knight(FigureType.Knight, WHITE, '♘'),
    W_Bishop(FigureType.Bishop, WHITE, '♗'),
    W_Rook(FigureType.Rook, WHITE, '♖'),
    W_Queen(FigureType.Queen, WHITE, '♕'),
    W_King(FigureType.King, WHITE, '♔'),

    B_Pawn(FigureType.Pawn, BLACK, '♟'),
    B_Knight(FigureType.Knight, BLACK, '♞'),
    B_Bishop(FigureType.Bishop, BLACK, '♝'),
    B_Rook(FigureType.Rook, BLACK, '♜'),
    B_Queen(FigureType.Queen, BLACK, '♛'),
    B_King(FigureType.King, BLACK, '♚');

    public final byte figureCode;
    public final char figureChar;
    public final FigureType figureType;
    public final Color color;
    public final char figureCharUnicode;

    Figure(FigureType figureType, Color color, char figureCharUnicode) {
        this.figureCharUnicode = figureCharUnicode;
        if (figureType == FigureType.EMPTY) {
            this.figureCode =  figureType.figureCode;
        } else {
            this.figureCode = (byte) (figureType.figureCode | color.code);
        }
        if (color == WHITE) {
            this.figureChar = figureType.figureChar;
        } else {
            this.figureChar = Character.toLowerCase(figureType.figureChar);
        }
        this.figureType = figureType;
        this.color = color;

        Index.figureCodeIndex[this.figureCode] = this;
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

    public static Color getColor(byte figure) {
        return (figure & BLACK.code) != 0? BLACK: WHITE;
    }

    static class Index {
        public final static Figure[] figureCodeIndex = new Figure[MAX_FIGURE_INDEX];
    }

    public static Figure getFigureByCode(byte figure) {
        Figure fig= Index.figureCodeIndex[figure];
        if (fig==null){
            throw new IllegalStateException("unknown figure code.. " + figure);
        }
        return fig;
    }

}
