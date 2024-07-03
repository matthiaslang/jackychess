package org.mattlang.tuning;

import static org.mattlang.jc.board.FigureConstants.FT_KING;

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

    @Getter
    private BitChessBoard board = new BitChessBoard();

    /** use byte  for castlingrights to save memory. */
    private byte castlingRights = CastlingRights.INITIAL_RIGHTS;

    private long zobristHash = 0L;

    private Material material = new Material();

    @Getter
    private Color siteToMove;

    private boolean chess960 = false;

    public BitBoardForTuning(BitChessBoard board, CastlingRights castlingRights, int enPassantMoveTargetPos,
            Color siteToMove) {
        this.board = board;
        this.castlingRights = castlingRights.getRights();
        this.siteToMove = siteToMove;
    }

    public BitBoardForTuning(BitChessBoard board, byte castlingRights, int enPassantMoveTargetPos,
            Color siteToMove) {
        this.board = board;
        this.castlingRights = castlingRights;
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
        return  board.equals(bitBoard.board)
                && castlingRights == bitBoard.castlingRights && siteToMove == bitBoard.siteToMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, castlingRights, siteToMove);
    }

    @Override
    public BitBoardForTuning copy() {
        BitBoardForTuning
                copied = new BitBoardForTuning(board.copy(), castlingRights, 0, siteToMove);
        copied.zobristHash = Zobrist.hash(copied);
        copied.material.init(copied);
        copied.chess960 = chess960;
        return copied;
    }

    public static BitBoardForTuning copy(BoardRepresentation bitBoard) {
        BitBoardForTuning
                copied =
                new BitBoardForTuning(bitBoard.getBoard().copy(), new CastlingRights(bitBoard.getCastlingRights()),
                        bitBoard.getEnPassantMoveTargetPos(), bitBoard.getSiteToMove());
        copied.zobristHash = bitBoard.getZobristHash();
        copied.material.init(copied);
        copied.chess960 = bitBoard.isChess960();
        //        copied.moveCounter = bitBoarmoveCounter;
        return copied;
    }

    @Override
    public int getKingPos(int color) {
        long kingBB = board.getPieceSet(FT_KING, color);
        return Long.numberOfTrailingZeros(kingBB);
    }

    @Override
    public boolean isEnPassantCapturePossible(int n) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public int getEnPassantCapturePos() {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public int getEnPassantMoveTargetPos() {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public void setEnPassantOption(int enPassantOption) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public boolean isCastlingAllowed(CastlingType castlingType) {
        return CastlingRights.isAllowed(castlingRights, castlingType);
    }

    @Override
    public void setCastlingAllowed(CastlingType castlingType, CastlingMove castlingMove) {
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
    }

    @Override
    public byte getCastlingRights() {
        return castlingRights;
    }

    @Override
    public long getZobristHash() {
        return zobristHash;
    }

    @Override
    public long getPawnKingZobristHash() {
        throw new IllegalStateException("not implemented!");
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
        throw new IllegalStateException("not allowed! this is only a immutable read only Board!");
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
