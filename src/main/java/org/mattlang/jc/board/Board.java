package org.mattlang.jc.board;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

import java.util.Arrays;
import java.util.Objects;

import org.mattlang.jc.uci.FenParser;

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

    private byte[] board = new byte[64];

    private Rochade whiteRochade = new Rochade();
    private Rochade blackRochace = new Rochade();

    public Board() {
    }

    public Board(byte[] board, Rochade whiteRochade, Rochade blackRochace) {
        this.board = board;
        this.whiteRochade = whiteRochade;
        this.blackRochace = blackRochace;
    }

    public void setStartPosition() {
        setPosition(FEN_START_POSITION);
        whiteRochade = new Rochade();
        blackRochace = new Rochade();
    }

    public void setPosition(String[] fenPosition) {
        for (int i = 0; i < 8; i++) {
            String row = expandRow(fenPosition[i]);
            for (int j = 0; j < 8; j++) {
                setPos(i, j, row.charAt(j));
            }
        }
    }

    public void setFenPosition(String fen) {
        FenParser parser = new FenParser();
        parser.setPosition(fen, this);
    }

    /**
     * Sets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @param figureChar
     */
    public void setPos(int row, int col, char figureChar) {
        board[(7 - row) * 8 + col] = Figure.convertFigureChar(figureChar);
    }

    public void setPos(int index, Figure figure) {
        board[index] = figure.figureCode;
    }

    public void setPos(int index, byte figure) {
        board[index] = figure;
    }

    /**
     * Gets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @return
     */
    public char getPos(int row, int col) {
        return Figure.toFigureChar(board[(7 - row) * 8 + col]);
    }

    public Figure getPos(int i) {
        return Figure.getFigureByCode(board[i]);
    }

    public Figure getFigurePos(int row, int col) {
        return Figure.getFigureByCode(board[(7 - row) * 8 + col]);
    }

    public void clearPosition() {
        for (int i = 0; i < board.length; i++) {
            board[i] = Figure.EMPTY.figureCode;
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

    public String toUniCodeStr() {
        return BoardPrinter.toUniCodeStr(this);
    }

    public Move move(Move move) {
        // todo validations?
        return move.move(this);
    }

    /**
     * Simple move of one figure from one field to another.
     *
     * @param from
     * @param to
     * @return the captured figure or empty
     */
    public byte move(int from, int to) {
        byte figure = board[from];
        board[from] = Figure.EMPTY.figureCode;
        byte capturedFigure = board[to];
        board[to] = figure;
        return capturedFigure;
    }

    public Figure getFigure(int i) {
        return Figure.getFigureByCode(board[i]);
    }

    public byte getFigureCode(int i) {
        return board[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Board board1 = (Board) o;
        return Arrays.equals(board, board1.board) &&
                whiteRochade.equals(board1.whiteRochade) &&
                blackRochace.equals(board1.blackRochace);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(whiteRochade, blackRochace);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }

    public Board copy() {
        return new Board(board.clone(), whiteRochade.copy(), blackRochace.copy());
    }
}
