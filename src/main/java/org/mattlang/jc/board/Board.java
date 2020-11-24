package org.mattlang.jc.board;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

/**
 * Represents a board with figures.
 */
public class Board {
    // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

    public static final String[] FEN_START_POSITION = {
            "rnbqkbnr",
            "pppppppp",
            "8",
            "8",
            "8",
            "8",
            "PPPPPPPP",
            "RNBQKBNR"
    };

    public static final byte Pawn = 1;
    public static final byte Rook = 2;
    public static final byte Knight = 4;
    public static final byte Bishop = 8;
    public static final byte Queen = 16;
    public static final byte King = 32;

    public static final byte BLACK = 64;

    private byte[] board = new byte[64];

    public void setStartPosition() {
        setPosition(FEN_START_POSITION);
    }

    public void setPosition(String[] fenPosition) {
        for (int i = 0; i < 8; i++) {
            String row = expandRow(fenPosition[i]);
            for (int j = 0; j < 8; j++) {
                setPos(i, j, row.charAt(j));
            }
        }
    }

    /**
     * Sets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @param figureChar
     */
    public void setPos(int row, int col, char figureChar) {
        board[(7 - row) * 8 + col] = convertFigureChar(figureChar);
    }

    /**
     * Gets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @return
     */
    public char getPos(int row, int col) {
        return toFigureChar(board[(7 - row) * 8 + col]);
    }

    private byte convertFigureChar(char figureChar) {
        byte figure = 0;
        switch (Character.toUpperCase(figureChar)) {
        case ' ':
            return 0;
        case 'P':
            figure = Pawn;
            break;
        case 'R':
            figure = Rook;
            break;
        case 'N':
            figure = Knight;
            break;
        case 'B':
            figure = Bishop;
            break;
        case 'Q':
            figure = Queen;
            break;
        case 'K':
            figure = King;
            break;
        default:
            throw new IllegalArgumentException("Unknown Figure: " + figureChar);

        }
        if (Character.isLowerCase(figureChar)) {
            figure |= BLACK;
        }
        return figure;
    }

    public void clearPosition() {
        for (int i = 0; i < board.length; i++) {
            board[i] = 0;
        }
    }

    private String expandRow(String row) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < row.length(); i++) {
            char ch = row.charAt(i);
            if (isDigit(ch)) {
                int empties = parseInt(String.valueOf(ch));
                for (int e = 1; e <= empties; e++) {
                    b.append(" ");
                }
            } else {
                b.append(ch);
            }
        }
        return b.toString();
    }

    public String toStr() {
        StringBuilder b = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                b.append(getPos(row, col));
            }
            b.append("\n");
        }

        return b.toString();
    }

    private char toFigureChar(byte figure) {
        if (figure == 0) {
            return ' ';
        }
        boolean black = figure >= BLACK;
        char figureChar = ' ';
        if (black) {
            figure = (byte) (0b111111 & figure);
        }
        switch (figure) {
        case Pawn:
            figureChar = 'P';
            break;
        case Rook:
            figureChar = 'R';
            break;
        case Knight:
            figureChar = 'N';
            break;
        case Bishop:
            figureChar = 'B';
            break;
        case Queen:
            figureChar = 'Q';
            break;
        case King:
            figureChar = 'K';
            break;
        default:
            throw new IllegalArgumentException("Unknown Figure: " + figure);
        }
        if (black) {
            figureChar = Character.toLowerCase(figureChar);
        }
        return figureChar;
    }

    public void move(Move move) {
        // todo validations?
        byte figure = board[move.getFromIndex()];
        board[move.getFromIndex()] = 0;
        board[move.getToIndex()] = figure;

    }
}
