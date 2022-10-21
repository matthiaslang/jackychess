package org.mattlang.attic.board;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.board.bitboard.BitBoard.MAXMOVES;

import java.util.Arrays;
import java.util.Objects;

import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.BoardCastlings;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.zobrist.Zobrist;

import lombok.Getter;

/**
 * Represents a board with figures.
 * This variant keeps a redundant piece list in addition to the board.
 * Expectation would be that certain actions like check tests, move generation would be a bit faster..
 *
 * this version holds a zobrist key for the board representation.
 *
 */
public class Board3 implements BoardRepresentation {

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
    public static final int NO_EN_PASSANT_OPTION = -1;

    private byte[] board = new byte[64];

    private CastlingRights castlingRights = new CastlingRights();

    private PieceList blackPieces = new PieceList();
    private PieceList whitePieces = new PieceList();

    private long zobristHash = 0L;

    @Getter
    private Color siteToMove;

    private int[] historyEp = new int[MAXMOVES];
    private long[] historyZobrist = new long[MAXMOVES];
    private byte[] historyCastling = new byte[MAXMOVES];

    private int moveCounter = 0;

    /**
     * the target pos of the en passant move that could be taken as next move on the board.
     * -1 if no en passant is possible.
     */
    private int enPassantMoveTargetPos = NO_EN_PASSANT_OPTION;

    private BoardCastlings boardCastlings = new BoardCastlings(this);

    public Board3() {
        for (int i = 0; i < 64; i++) {
            board[i] = FT_EMPTY;
        }
        siteToMove = WHITE;
    }

    public Board3(byte[] board, CastlingRights castlingRights, int enPassantMoveTargetPos, Color siteToMove) {
        this.board = board;
        this.castlingRights = castlingRights;
        this.enPassantMoveTargetPos = enPassantMoveTargetPos;
        this.siteToMove = siteToMove;

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
    }

    @Override
    public void setPosition(String[] fenPosition) {
        for (int i = 0; i < 8; i++) {
            String row = expandRow(fenPosition[i]);
            for (int j = 0; j < 8; j++) {
                setPos(i, j, row.charAt(j));
            }
        }
        moveCounter=0;
        siteToMove=WHITE;
        castlingRights = new CastlingRights();
        boardCastlings=new BoardCastlings(this);
        zobristHash = Zobrist.hash(this);
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
            zobristHash = Zobrist.removeFig(zobristHash, pos, oldFigure);
        }
        // add this piece to piece list:
        if (figureCode != FigureConstants.FT_EMPTY && figureCode != 0) {
            PieceList pieceList = ((figureCode & BLACK.code) == BLACK.code) ? blackPieces : whitePieces;
            pieceList.set(pos, (byte) (figureCode & MASK_OUT_COLOR));
            zobristHash = Zobrist.addFig(zobristHash, pos, figureCode);
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
        Arrays.fill(board, Figure.EMPTY.figureCode);
        cleanPeaceList();
        moveCounter=0;
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
    public void println() {
        System.out.println(toUniCodeStr());
    }

    /**
     * Simple move of one figure from one field to another.
     *
     * @param from
     * @param to
     */
    private void move(int from, int to) {
        byte figure = board[from];
        // remove castling rights when rooks or kings are moved:
        if (figure == W_KING) {
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            castlingRights.retain(WHITE, SHORT);
            castlingRights.retain(WHITE, LONG);
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        } else if (figure == B_KING) {
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            castlingRights.retain(BLACK, SHORT);
            castlingRights.retain(BLACK, LONG);
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        } else if (figure == W_ROOK && from == 0) {
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            castlingRights.retain(WHITE, LONG);
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        } else if (figure == W_ROOK && from == 7) {
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            castlingRights.retain(WHITE, SHORT);
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        } else if (figure == B_ROOK && from == 56) {
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            castlingRights.retain(BLACK, LONG);
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        } else if (figure == B_ROOK && from == 63) {
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            castlingRights.retain(BLACK, SHORT);
            zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        }

        set(from, Figure.EMPTY.figureCode);
        set(to, figure);

        resetEnPassant();

        // check double pawn move. here we need to mark an possible en passant follow up move:
        // be careful: we must not set the en passant option by undoing a double pawn move:
        if (figure == W_PAWN && to - from == 16) {
            setEnPassantOption((from + to) / 2);
        } else if (figure == B_PAWN && from - to == 16) {
            setEnPassantOption((from + to) / 2);
        }

    }

    @Override
    public void switchSiteToMove() {
        siteToMove = siteToMove.invert();
        zobristHash = Zobrist.colorFlip(zobristHash);
    }

    @Override
    public Figure getFigure(int i) {
        return Figure.getFigureByCode(board[i]);
    }

    @Override
    public final byte getFigureCode(int i) {
        return board[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board3 board1 = (Board3) o;
        return enPassantMoveTargetPos == board1.enPassantMoveTargetPos &&
                Arrays.equals(board, board1.board) &&
                castlingRights.equals(board1.castlingRights);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(castlingRights, enPassantMoveTargetPos);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }

    @Override
    public Board3 copy() {
        Board3 copied = new Board3(board.clone(), castlingRights.copy(), enPassantMoveTargetPos, siteToMove);
        copied.zobristHash = Zobrist.hash(copied);
        copied.historyCastling=historyCastling.clone();
        copied.historyZobrist=historyZobrist.clone();
        copied.historyEp=historyEp.clone();
        copied.boardCastlings.initFrom(boardCastlings);
        copied.moveCounter=moveCounter;
        return copied;
    }

    @Override
    public int findPosOfFigure(byte figureCode) {
        // refactor to "findKingPos"
        PieceList pieceList = ((figureCode & BLACK.code) == BLACK.code) ? blackPieces : whitePieces;
        return pieceList.getKing();
    }

    @Override
    public boolean isEnPassantCapturePossible(int n) {
        return enPassantMoveTargetPos == n;
    }

    @Override
    public int getEnPassantCapturePos() {
        return calcEnPassantCaptureFromEnPassantOption();
    }

    @Override
    public int getEnPassantMoveTargetPos() {
        return enPassantMoveTargetPos;
    }

    @Override
    public void setEnPassantOption(int enPassantOption) {
        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
        this.enPassantMoveTargetPos = enPassantOption;

        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
    }

    private int calcEnPassantCaptureFromEnPassantOption(){
        if (enPassantMoveTargetPos >=16 && enPassantMoveTargetPos<=23) {
            return enPassantMoveTargetPos + 8;
        } else {
            return enPassantMoveTargetPos - 8;
        }
    }

    private void resetEnPassant() {
        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
        enPassantMoveTargetPos = NO_EN_PASSANT_OPTION;
        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
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
    public void setCastlingAllowed(CastlingType castlingType, CastlingMove castlingMove) {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.setAllowed(castlingType);
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());

        boardCastlings.setCastlingMove(castlingType, castlingMove);
    }

    @Override
    public byte getCastlingRights() {
        return castlingRights.getRights();
    }

    @Override
    public long getZobristHash() {
        return zobristHash;
    }

    @Override
    public void domove(Move move) {
        historyCastling[moveCounter] = castlingRights.getRights();
        historyEp[moveCounter] = enPassantMoveTargetPos;
        historyZobrist[moveCounter] = zobristHash;
        moveCounter++;

        move(move.getFromIndex(), move.getToIndex());
        if (move.isEnPassant()) {
            setPos(move.getEnPassantCapturePos(), FigureConstants.FT_EMPTY);
        } else if (move.isPromotion()) {
            setPos(move.getToIndex(), move.getPromotedFigureByte());
        } else if (move.isCastling()) {
            CastlingMove castlingMove = getCastlingMove(move);
            move(castlingMove.getRookFrom(), castlingMove.getRookTo());
        }

        switchSiteToMove();
    }

    @Override
    public void undo(Move move) {
        move(move.getToIndex(), move.getFromIndex());
        if (move.getCapturedFigure() != 0) {
            setPos(move.getToIndex(), move.getCapturedFigure());
        }
        if (move.isEnPassant()) {
            // override the "default" overrider field with empty..
            setPos(move.getToIndex(), FigureConstants.FT_EMPTY);
            // because we have the special en passant capture pos which we need to reset with the captured figure
            setPos(move.getEnPassantCapturePos(), move.getCapturedFigure());
        } else if (move.isPromotion()) {
            Figure promotedFigure = getFigure(move.getFromIndex());
            Figure pawn = promotedFigure.color == Color.WHITE ? Figure.W_Pawn : Figure.B_Pawn;
            setPos(move.getFromIndex(), pawn);
        } else if (move.isCastling()) {
            CastlingMove castlingMove = getCastlingMove(move);
            move(castlingMove.getRookTo(), castlingMove.getRookFrom());
        }

        moveCounter--;
        castlingRights.setRights(historyCastling[moveCounter]);
        enPassantMoveTargetPos = historyEp[moveCounter];
        zobristHash = historyZobrist[moveCounter];
    }


    @Override
    public void undoNullMove() {

        siteToMove = siteToMove.invert();

        moveCounter--;
        castlingRights.setRights(historyCastling[moveCounter]);
        enPassantMoveTargetPos = historyEp[moveCounter];
        zobristHash = historyZobrist[moveCounter];
    }

    @Override
    public void doNullMove() {
        historyCastling[moveCounter] = castlingRights.getRights();
        historyEp[moveCounter] = enPassantMoveTargetPos;
        historyZobrist[moveCounter] = zobristHash;
        moveCounter++;

        switchSiteToMove();
    }

    @Override
    public boolean isRepetition() {
        // if we would have infos about the latest move, we could also use this condition first:
        //        if (!MoveUtil.isQuiet(move) || figure == PAWN) {
        //            return false;
        //        }
        final int moveCountMin = Math.max(0, moveCounter - 50);
        for (int i = moveCounter - 2; i >= moveCountMin; i -= 2) {
            if (zobristHash == historyZobrist[i]) {
                return true;
            }
        }
        return false;

    }

    @Override
    public BitChessBoard getBoard() {
        throw new IllegalStateException("not implemented! This is no bitboard implementation");
    }

    @Override
    public boolean isvalidmove(int pvMove) {
        throw new IllegalStateException("not implemented!");
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public boolean isDrawByMaterial() {
        // not implemented, so return always false:
        return false;
    }

    @Override
    public BoardCastlings getBoardCastlings() {
        return boardCastlings;
    }

    private CastlingMove getCastlingMove(Move move) {
        return boardCastlings.getCastlingMove(move.getCastlingType());
    }

    @Override
    public void clearCastlingRights() {
        castlingRights.clearCastlingRights();
    }
}
