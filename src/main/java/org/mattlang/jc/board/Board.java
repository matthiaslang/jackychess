package org.mattlang.jc.board;

import org.mattlang.jc.uci.FenParser;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

/**
 * Represents a board with figures.
 */
public class Board implements BoardRepresentation {

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

    @Override
    public void setStartPosition() {
        setPosition(FEN_START_POSITION);
        whiteRochade = new Rochade();
        blackRochace = new Rochade();
    }

    @Override
    public void setPosition(String[] fenPosition) {
        for (int i = 0; i < 8; i++) {
            String row = expandRow(fenPosition[i]);
            for (int j = 0; j < 8; j++) {
                setPos(i, j, row.charAt(j));
            }
        }
    }

    @Override
    public GameState setFenPosition(String fen) {
        FenParser parser = new FenParser();
        return parser.setPosition(fen, this);
    }

    /**
     * Sets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @param figureChar
     */
    @Override
    public void setPos(int row, int col, char figureChar) {
        board[(7 - row) * 8 + col] = Figure.convertFigureChar(figureChar);
    }

    @Override
    public void setPos(int index, Figure figure) {
        board[index] = figure.figureCode;
    }

    @Override
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
    @Override
    public char getPos(int row, int col) {
        return Figure.toFigureChar(board[(7 - row) * 8 + col]);
    }

    @Override
    public Figure getPos(int i) {
        return Figure.getFigureByCode(board[i]);
    }

    @Override
    public Figure getFigurePos(int row, int col) {
        return Figure.getFigureByCode(board[(7 - row) * 8 + col]);
    }

    @Override
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

    @Override
    public String toUniCodeStr() {
        return BoardPrinter.toUniCodeStr(this);
    }

    @Override
    public Move move(Move move) {
        // todo validations?
        Move moveRslt= move.move(this);
        return moveRslt;
    }

    /**
     * Simple move of one figure from one field to another.
     *
     * @param from
     * @param to
     * @return the captured figure or empty
     */
    @Override
    public byte move(int from, int to) {
        byte figure = board[from];
        board[from] = Figure.EMPTY.figureCode;
        byte capturedFigure = board[to];
        board[to] = figure;

        resetEnPassant();

        return capturedFigure;
    }

    @Override
    public Figure getFigure(int i) {
        return Figure.getFigureByCode(board[i]);
    }

    @Override
    public byte getFigureCode(int i) {
        return board[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return enPassantMoveTargetPos == board1.enPassantMoveTargetPos &&
                enPassantCapturePos == board1.enPassantCapturePos &&
                Arrays.equals(board, board1.board) &&
                whiteRochade.equals(board1.whiteRochade) &&
                blackRochace.equals(board1.blackRochace);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(whiteRochade, blackRochace, enPassantMoveTargetPos, enPassantCapturePos);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }

    @Override
    public Board copy() {
        return new Board(board.clone(), whiteRochade.copy(), blackRochace.copy());
    }

    @Override
    public int findPosOfFigure(byte figureCode) {
        for (int i = 0; i < 64; i++) {
            if (board[i] == figureCode) {
                return i;
            }
        }
        return -1;
    }

    /**
     * the target pos of the en passant move that could be taken as next move on the board.
     * -1 if no en passant is possible.
     */
    private int enPassantMoveTargetPos = -1;
    /**
     * the capture pos of an en passant move (if an en passant move would be possible as next move on the board).
     * -1 if no en passant is possible.
     */
    private int enPassantCapturePos = -1;

    @Override
    public boolean isEnPassantCapturePossible(int n) {
        return enPassantMoveTargetPos == n;
    }

    @Override
    public int getEnPassantCapturePos() {
        return enPassantCapturePos;
    }

    @Override
    public void setEnPassantOption(int enPassantOption) {
        this.enPassantMoveTargetPos = enPassantOption;
        if (enPassantMoveTargetPos >=16 && enPassantCapturePos<=23) {
            enPassantCapturePos = enPassantMoveTargetPos + 8;
        } else {
            enPassantCapturePos = enPassantMoveTargetPos - 8;
        }
    }


    private void resetEnPassant() {
        enPassantMoveTargetPos = -1;
        enPassantCapturePos = -1;
    }
}
