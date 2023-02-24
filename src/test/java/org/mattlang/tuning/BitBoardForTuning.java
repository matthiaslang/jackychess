package org.mattlang.tuning;

import java.util.Objects;

import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.BoardCastlings;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.zobrist.Zobrist;

import lombok.Getter;

/**
 * Special board impl for tuning which uses less memory by not holding history (since this is not needed during tuning).
 * Changing methods are not implemented, so its effective an immutable board holding a position.
 */
public class BitBoardForTuning implements BoardRepresentation {

    public static final int NO_EN_PASSANT_OPTION = -1;

    @Getter
    private BitChessBoard board = new BitChessBoard();

    private CastlingRights castlingRights = new CastlingRights();

    private long zobristHash = 0L;

    private Material material = new Material();

    @Getter
    private Color siteToMove;
    /**
     * the target pos of the en passant move that could be taken as next move on the board.
     * -1 if no en passant is possible.
     */
    private int enPassantMoveTargetPos = NO_EN_PASSANT_OPTION;

    private BoardCastlings boardCastlings = new BoardCastlings(this);
    private boolean chess960 = false;

    public BitBoardForTuning(BitChessBoard board, CastlingRights castlingRights, int enPassantMoveTargetPos,
            Color siteToMove) {
        this.board = board;
        this.castlingRights = castlingRights;
        this.enPassantMoveTargetPos = enPassantMoveTargetPos;
        this.siteToMove = siteToMove;

    }

    @Override
    public void setStartPosition() {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public void setPosition(String[] fenPosition) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public GameState setFenPosition(String fen) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
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
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public void setPos(int index, Figure figure) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
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
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public String toUniCodeStr() {
        return BoardPrinter.toUniCodeStr(this);
    }

    @Override
    public void println() {
        System.out.println(toUniCodeStr());
    }

    @Override
    public void switchSiteToMove() {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
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
        BitBoardForTuning bitBoard = (BitBoardForTuning) o;
        return enPassantMoveTargetPos == bitBoard.enPassantMoveTargetPos && board.equals(bitBoard.board)
                && castlingRights.equals(bitBoard.castlingRights) && siteToMove == bitBoard.siteToMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, castlingRights, siteToMove, enPassantMoveTargetPos);
    }

    @Override
    public BitBoardForTuning copy() {
        BitBoardForTuning
                copied = new BitBoardForTuning(board.copy(), castlingRights.copy(), enPassantMoveTargetPos, siteToMove);
        copied.zobristHash = Zobrist.hash(copied);
        copied.material.init(copied);
        copied.chess960=chess960;
        return copied;
    }

    public static BitBoardForTuning copy(BoardRepresentation bitBoard) {
        BitBoardForTuning
                copied = new BitBoardForTuning(bitBoard.getBoard().copy(), new CastlingRights(bitBoard.getCastlingRights()), bitBoard.getEnPassantMoveTargetPos(), bitBoard.getSiteToMove());
        copied.zobristHash = Zobrist.hash(copied);
        copied.material.init(copied);
        copied.chess960=bitBoard.isChess960();
//        copied.moveCounter = bitBoarmoveCounter;
        return copied;
    }

    @Override
    public int findPosOfFigure(byte figureCode) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
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
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    private int calcEnPassantCaptureFromEnPassantOption() {
        if (enPassantMoveTargetPos >= 16 && enPassantMoveTargetPos <= 23) {
            return enPassantMoveTargetPos + 8;
        } else {
            return enPassantMoveTargetPos - 8;
        }
    }

    @Override
    public boolean isCastlingAllowed(Color color, RochadeType type) {
        return castlingRights.isAllowed(color, type);
    }

    @Override
    public void setCastlingAllowed(CastlingType castlingType, CastlingMove castlingMove) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
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
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public void undo(Move move) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }


    @Override
    public void undoNullMove() {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public void doNullMove() {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public boolean isRepetition() {
        throw new IllegalStateException("isRepetition not implemented!");
    }

    @Override
    public boolean isvalidmove(Color color, int aMove) {
        throw new IllegalStateException("not implemented! this is only a immutable read only Board!");
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public boolean isDrawByMaterial() {
        return material.isDrawByMaterial();
    }

    public void doAssertLogs() {
        try {
            board.doAssertions();
        } catch (AssertionError e) {
            println();
            e.printStackTrace();
        }
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
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public boolean isChess960() {
        return chess960;
    }

    @Override
    public void setChess960(boolean chess960) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }
}
