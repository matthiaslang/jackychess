package org.mattlang.jc.board.bitboard;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;

import java.util.Objects;

import org.mattlang.jc.board.*;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.zobrist.Zobrist;

import lombok.Getter;

public class BitBoard implements BoardRepresentation {

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
    public static final int MAXMOVES = 1024;

    @Getter
    private BitChessBoard board = new BitChessBoard();

    private CastlingRights castlingRights = new CastlingRights();

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

    public BitBoard() {
        for (int i = 0; i < 64; i++) {
            board.set(i, FT_EMPTY);
        }
        siteToMove = WHITE;
    }

    public BitBoard(BitChessBoard board, CastlingRights castlingRights, int enPassantMoveTargetPos, Color siteToMove) {
        this.board = board;
        this.castlingRights = castlingRights;
        this.enPassantMoveTargetPos = enPassantMoveTargetPos;
        this.siteToMove = siteToMove;

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
        zobristHash = Zobrist.hash(this);
    }


    @Override
    public GameState setFenPosition(String fen) {
        FenParser parser = new FenParser();
        return parser.setPosition(fen, this);
    }

    private void set(int pos, byte figureCode) {
        byte oldFigure = board.get(pos);
        board.set(pos, figureCode);
        // remove from piece list, if this is a "override/capture" of this field:
        if (oldFigure != FigureConstants.FT_EMPTY && oldFigure != 0) {
            zobristHash = Zobrist.removeFig(zobristHash, pos, oldFigure);
        }
        // add this piece to piece list:
        if (figureCode != FigureConstants.FT_EMPTY && figureCode != 0) {
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
        return Figure.toFigureChar(board.get((7 - row) * 8 + col));
    }

    @Override
    public Figure getPos(int i) {
        return Figure.getFigureByCode(board.get(i));
    }

    @Override
    public Figure getFigurePos(int row, int col) {
        return Figure.getFigureByCode(board.get((7 - row) * 8 + col));
    }

    @Override
    public void clearPosition() {
        for (int i = 0; i < 64; i++) {
            board.set(i, Figure.EMPTY.figureCode);
        }
        moveCounter = 0;
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

    /**
     * Simple move of one figure from one field to another.
     *
     * @param from
     * @param to
     */
    @Override
    public void move(int from, int to) {
        byte figure = board.get(from);
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

    /**
     * used during undoing. here we need not to take care about updating zobrist key, as it is reset afterwards
     * anyway to the old value.
     *
     * @param from
     * @param to
     */
    private void undomove(int from, int to) {
        byte figure = board.get(from);
        board.set(from, Figure.EMPTY.figureCode);
        board.set(to, figure);
    }

    @Override
    public void switchSiteToMove() {
        siteToMove = siteToMove.invert();
        zobristHash = Zobrist.colorFlip(zobristHash);
    }

    @Override
    public Figure getFigure(int i) {
        return Figure.getFigureByCode(board.get(i));
    }

    @Override
    public final byte getFigureCode(int i) {
        return board.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BitBoard bitBoard = (BitBoard) o;
        return enPassantMoveTargetPos == bitBoard.enPassantMoveTargetPos && board.equals(bitBoard.board)
                && castlingRights.equals(bitBoard.castlingRights) && siteToMove == bitBoard.siteToMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, castlingRights, siteToMove, enPassantMoveTargetPos);
    }

    @Override
    public BitBoard copy() {
        BitBoard copied = new BitBoard(board.copy(), castlingRights.copy(), enPassantMoveTargetPos, siteToMove);
        copied.zobristHash = Zobrist.hash(copied);
        return copied;
    }

    @Override
    public int findPosOfFigure(byte figureCode) {
        // todo that method should be renamed to something like getKingPos()
        long kingBB = board.getPieceSet(FT_KING, (figureCode & BLACK.code)==BLACK.code?BLACK:WHITE);
        final int king = Long.numberOfTrailingZeros(kingBB);
        return king;
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

    private int calcEnPassantCaptureFromEnPassantOption() {
        if (enPassantMoveTargetPos >= 16 && enPassantMoveTargetPos <= 23) {
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
        return createPieceList(BLACK);
    }

    public PieceList getWhitePieces() {
        return createPieceList(WHITE);
    }

    private PieceList createPieceList(Color side) {
        PieceList pieceList = new PieceList();
        BitChessBoard bb = board;

        long pawnBB = bb.getPieceSet(FT_PAWN, side);
        while (pawnBB != 0) {
            final int pawn = Long.numberOfTrailingZeros(pawnBB);
            pieceList.set(pawn, FT_PAWN);
            pawnBB &= pawnBB - 1;
        }

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);
            pieceList.set(bishop, FT_BISHOP);
            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);
            pieceList.set(knight, FT_KNIGHT);
            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);
            pieceList.set(rook, FT_ROOK);
            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);
            pieceList.set(queen, FT_QUEEN);
            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);
        pieceList.setKing(king);

        return pieceList;
    }

    @Override
    public boolean isCastlingAllowed(Color color, RochadeType type) {
        return castlingRights.isAllowed(color, type);
    }

    @Override
    public void setCastlingAllowed(Color color, RochadeType type) {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.setAllowed(color, type);
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
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
            CastlingMove castlingMove = move.getCastlingMove();
            move(castlingMove.getFromIndex2(), castlingMove.getToIndex2());
        }

        switchSiteToMove();
    }

    @Override
    public void undo(Move move) {
        undomove(move.getToIndex(), move.getFromIndex());
        if (move.getCapturedFigure() != 0) {
            board.set(move.getToIndex(), move.getCapturedFigure());
        }
        if (move.isEnPassant()) {
            // override the "default" overrider field with empty..
            board.set(move.getToIndex(), FigureConstants.FT_EMPTY);
            // because we have the special en passant capture pos which we need to reset with the captured figure
            board.set(move.getEnPassantCapturePos(), move.getCapturedFigure());
        } else if (move.isPromotion()) {
            Figure promotedFigure = getFigure(move.getFromIndex());
            byte pawn = promotedFigure.color == Color.WHITE ? Figure.W_Pawn.figureCode : Figure.B_Pawn.figureCode;
            board.set(move.getFromIndex(), pawn);
        } else if (move.isCastling()) {
            CastlingMove castlingMove = move.getCastlingMove();
            undomove(castlingMove.getToIndex2(), castlingMove.getFromIndex2());
        }

        siteToMove = siteToMove.invert();

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

}
