package org.mattlang.jc.board;

import org.mattlang.jc.uci.FenParser;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;

/**
 * Represents a board with figures.
 * This variant keeps a redundant piece list in addition to the board.
 * Expectation would be that certain actions like check tests, move generation would be a bit faster..
 *
 */
public class Board2 implements BoardRepresentation {

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

    private CastlingRights castlingRights= new CastlingRights();

    private PieceList blackPieces = new PieceList();
    private PieceList whitePieces = new PieceList();


    public Board2() {
    }

    public Board2(byte[] board, CastlingRights castlingRights) {
        this.board = board;
        this.castlingRights = castlingRights;

        initPeaceList();
    }

    private void initPeaceList() {
        for(int i=0; i<64; i++) {
            if (board[i] != FigureConstants.FT_EMPTY) {
                Figure figure = getFigure(i);
                PieceList pieceList = figure.color == Color.WHITE ? whitePieces: blackPieces;

                pieceList.set(i, figure.figureType.figureCode);
            }
        }
    }

    @Override
    public void setStartPosition() {
        setPosition(FEN_START_POSITION);
        castlingRights = new CastlingRights();
    }

    @Override
    public void setPosition(String[] fenPosition) {
        cleanPeaceList();
        for (int i = 0; i < 8; i++) {
            String row = expandRow(fenPosition[i]);
            for (int j = 0; j < 8; j++) {
                setPos(i, j, row.charAt(j));
            }
        }
    }

    private void cleanPeaceList() {
        whitePieces.clean();
        blackPieces.clean();
    }

    @Override
    public GameState setFenPosition(String fen) {
        FenParser parser = new FenParser();
        return parser.setPosition(fen, this);
    }

    private void set(int pos, byte figureCode) {
        byte oldFigure = board[pos];
        board[pos] = figureCode;
        // remove from piece list, if this is a "override/capture" of this field:
        if (oldFigure != FigureConstants.FT_EMPTY && oldFigure != 0) {
            PieceList pieceList = ((oldFigure & BLACK.code) == BLACK.code) ? blackPieces : whitePieces;
            pieceList.remove(pos, (byte) (oldFigure & MASK_OUT_COLOR));
        }
        // add this piece to piece list:
        if (figureCode != FigureConstants.FT_EMPTY && figureCode != 0) {
            PieceList pieceList = ((figureCode & BLACK.code) == BLACK.code) ? blackPieces : whitePieces;
            pieceList.set(pos, (byte) (figureCode & MASK_OUT_COLOR));
        }
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
        set((7 - row) * 8 + col, Figure.convertFigureChar(figureChar));
    }

    @Override
    public void setPos(int index, Figure figure) {
        set(index, figure.figureCode);
    }

    @Override
    public void setPos(int index, byte figure) {
        set(index, figure);
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
        cleanPeaceList();
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
    public void move(Move move) {
        // todo validations?
        move.move(this);
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
        // remove castling rights when rooks or kings are moved:
        if (figure == W_KING) {
            castlingRights.retain(WHITE, SHORT);
            castlingRights.retain(WHITE, LONG);
        } else if (figure == B_KING) {
            castlingRights.retain(BLACK, SHORT);
            castlingRights.retain(BLACK, LONG);
        } else if (figure == W_ROOK && from == 0) {
            castlingRights.retain(WHITE, LONG);
        } else if (figure == W_ROOK && from == 7) {
            castlingRights.retain(WHITE, SHORT);
        } else if (figure == B_ROOK && from == 56) {
            castlingRights.retain(BLACK, LONG);
        } else if (figure == B_ROOK && from == 63) {
            castlingRights.retain(BLACK, SHORT);
        }

        set(from, Figure.EMPTY.figureCode);
        byte capturedFigure = board[to];
        set(to, figure);

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
        Board2 board1 = (Board2) o;
        return enPassantMoveTargetPos == board1.enPassantMoveTargetPos &&
                enPassantCapturePos == board1.enPassantCapturePos &&
                Arrays.equals(board, board1.board) &&
                castlingRights.equals(board1.castlingRights);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(castlingRights, enPassantMoveTargetPos, enPassantCapturePos);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }

    @Override
    public Board2 copy() {
        return new Board2(board.clone(), castlingRights.copy());
    }

    @Override
    public int findPosOfFigure(byte figureCode) {
        // refactor to "findKingPos"
        PieceList pieceList = ((figureCode & BLACK.code) == BLACK.code) ? blackPieces : whitePieces;
        return pieceList.getKing();
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
        if (enPassantMoveTargetPos >=16 && enPassantMoveTargetPos<=23) {
            enPassantCapturePos = enPassantMoveTargetPos + 8;
        } else {
            enPassantCapturePos = enPassantMoveTargetPos - 8;
        }
    }


    private void resetEnPassant() {
        enPassantMoveTargetPos = -1;
        enPassantCapturePos = -1;
    }

    public PieceList getBlackPieces() {
        return blackPieces;
    }

    public PieceList getWhitePieces() {
        return whitePieces;
    }

    @Override
    public boolean isCastlingAllowed(Color color, RochadeType type) {
        return castlingRights.isAllowed(color, type);
    }

    @Override
    public void setCastlingAllowed(Color color, RochadeType type) {
       castlingRights.setAllowed(color, type);
    }

    @Override
    public byte getCastlingRights() {
        return castlingRights.getRights();
    }

    @Override
    public void setCastlingRights(byte newCastlingRights) {
        castlingRights.setRights(newCastlingRights);
    }
}
