package org.mattlang.jc.board;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

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
        /*
        byte figure = board[move.getFromIndex()];
        byte override = move(move.getFromIndex(), move.getToIndex());
        if (move.isPawnPromotion()) {
            Figure pawn = Figure.getFigureByCode(figure);
            Figure queen = pawn.color == Color.WHITE ? Figure.Queen : Figure.B_Queen;
            board[move.getToIndex()] = queen.figureCode;
        }

        if (move instanceof UndoMove) {
            UndoMove undoMove = (UndoMove) move;
            board[move.getFromIndex()] = undoMove.overriddenFig;
            if (undoMove.undoPawnPromotion) {
                Figure queen = Figure.getFigureByCode(board[move.getToIndex()]);
                Figure pawn = queen.color == Color.WHITE ? Figure.Pawn : Figure.B_Pawn;
                board[move.getToIndex()] = pawn.figureCode;
            }
        }

        if (move instanceof RochadeMove) {
            RochadeMove rochadeMove = (RochadeMove) move;
            Move second = rochadeMove.getSecond();
            move(second.getFromIndex(), second.getToIndex());

            return new RochadeUndoMove(move.getToIndex(), move.getFromIndex(), second.getFromIndex(), second
                    .getToIndex());
        } else {
            return new UndoMove(move.getToIndex(), move.getFromIndex(), override, move.isPawnPromotion());
        } */
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
}
