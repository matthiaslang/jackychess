package org.mattlang.jc.moves;

import static org.mattlang.jc.board.IndexConversion.convert;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import java.util.Objects;

import org.mattlang.jc.board.*;

import lombok.Getter;

/**
 * Represents a move on the board.
 * <p>
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 */
@Getter
public class MoveImpl implements Move {

    public static final int NOT_SORTED = Integer.MAX_VALUE;
    private static final byte NO_EN_PASSANT_OPTION = 100;

    private byte figureType;

    private byte fromIndex;

    private byte toIndex;

    /**
     * optional en passant option (for a pawn move doing a two steps move).
     */
    private byte enPassantOption = NO_EN_PASSANT_OPTION;

    /**
     * capture pos for an en passant move.
     */
    private byte enPassantCapturePos;

    private byte capturedFigure;

    /**
     * the promoted figure for pawn promotion moves.
     */
    private byte promotedFigure;

    /**
     * type of move.
     */
    private byte type = NORMAL_MOVE;

    public static final byte NORMAL_MOVE = 1;
    public static final byte ENPASSANT_MOVE = 2;
    public static final byte PAWN_PROMOTION_MOVE = 4;
    public static final byte CASTLING_WHITE_LONG = 8;
    public static final byte CASTLING_WHITE_SHORT = 16;
    public static final byte CASTLING_BLACK_SHORT = 32;
    public static final byte CASTLING_BLACK_LONG = 64;

    private int order = NOT_SORTED;

    public MoveImpl(long l) {
        fromLongEncoded(l);
    }

    public MoveImpl(String moveStr) {
        fromIndex = parsePos(moveStr.substring(0, 2));
        toIndex = parsePos((moveStr.substring(2, 4)));
    }



    public MoveImpl(byte figureType, int from, int to, byte capturedFigure) {
        this.figureType = figureType;
        this.fromIndex = (byte) from;
        this.toIndex = (byte) to;
        this.capturedFigure = capturedFigure;
    }

    public MoveImpl(byte figureType, int from, int to, byte capturedFigure, int enPassantOption) {
        this(figureType, from, to, capturedFigure);
        this.enPassantOption = (byte) enPassantOption;
    }

    private MoveImpl(int from, int to, byte capturedFigure, Figure promotedFigure) {
        this(FigureConstants.FT_PAWN, from, to, capturedFigure);
        this.promotedFigure = promotedFigure.figureCode;
        this.type = PAWN_PROMOTION_MOVE;
    }

    private MoveImpl(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        this(FigureConstants.FT_PAWN, from, to, capturedFigure);
        this.enPassantCapturePos = (byte) enPassantCapturePos;
        this.type = ENPASSANT_MOVE;
    }

    private MoveImpl(CastlingMove castlingMove) {
        this.type = castlingMove.getType();
        this.fromIndex = castlingMove.getFromIndex();
        this.toIndex = castlingMove.getToIndex();
    }

    public static MoveImpl createCastling(CastlingMove castlingMove){
        return new MoveImpl(castlingMove);
    }

    public static MoveImpl createPromotion(int from, int to, byte capturedFigure, Figure promotedFigure) {
        return new MoveImpl(from, to, capturedFigure, promotedFigure);
    }

    public static MoveImpl createEnPassant(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        return new MoveImpl(from, to, capturedFigure, enPassantCapturePos);
    }

    public static long createNormalMove(byte figureType, int fromIndex, int toIndex, byte capturedFigure,
            int enPassantOption
    ) {
        return longRepresentation(NORMAL_MOVE, figureType, (byte)fromIndex, (byte)toIndex, (byte)enPassantOption, (byte) 0,
                capturedFigure, (byte) 0);
    }

    public static long createCastlingMove(CastlingMove castlingMove) {
        return longRepresentation(castlingMove.getType(), (byte) 0, castlingMove.getFromIndex(),
                castlingMove.getToIndex(), (byte) 0, (byte) 0,
                (byte) 0, (byte) 0);
    }

    public static long createPromotionMove(int from, int to, byte capturedFigure, Figure promotedFigure) {
        return longRepresentation(PAWN_PROMOTION_MOVE, FigureConstants.FT_PAWN, (byte) from, (byte) to, (byte) 0,
                (byte) 0,
                capturedFigure, promotedFigure.figureCode);
    }

    public static long createEnPassantMove(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        return longRepresentation(ENPASSANT_MOVE, FigureConstants.FT_PAWN, (byte) from, (byte) to, (byte) 0,
                (byte) enPassantCapturePos,
                capturedFigure, (byte) 0);
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public String toStr() {
        return convert(fromIndex) + convert(toIndex);
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return toStr();
    }

    @Override
    public void move(BoardRepresentation board) {
        board.move(getFromIndex(), getToIndex());
        if (enPassantOption != NO_EN_PASSANT_OPTION) {
            board.setEnPassantOption(enPassantOption);
        }
        switch (type) {
        case ENPASSANT_MOVE:
            board.setPos(enPassantCapturePos, FigureConstants.FT_EMPTY);
            break;
        case PAWN_PROMOTION_MOVE:
            board.setPos(getToIndex(), promotedFigure);
            break;

        case CASTLING_WHITE_LONG:
            CastlingMove.CASTLING_WHITE_LONG.moveSecond(board);
            break;
        case CASTLING_WHITE_SHORT:
            CastlingMove.CASTLING_WHITE_SHORT.moveSecond(board);
            break;
        case CASTLING_BLACK_SHORT:
            CastlingMove.CASTLING_BLACK_SHORT.moveSecond(board);
            break;
        case CASTLING_BLACK_LONG:
            CastlingMove.CASTLING_BLACK_LONG.moveSecond(board);
            break;
        }
    }

    @Override
    public void undo(BoardRepresentation board) {
        board.move(getToIndex(), getFromIndex());
        if (capturedFigure != 0) {
            board.setPos(getToIndex(), capturedFigure);
        }

        switch (type) {
        case ENPASSANT_MOVE:
            // override the "default" overrider field with empty..
            board.setPos(getToIndex(), FigureConstants.FT_EMPTY);
            // because we have the special en passant capture pos which we need to reset with the captured figure
            board.setPos(enPassantCapturePos, getCapturedFigure());
            break;

        case PAWN_PROMOTION_MOVE:
            Figure promotedFigure = board.getFigure(getFromIndex());
            Figure pawn = promotedFigure.color == Color.WHITE ? Figure.W_Pawn : Figure.B_Pawn;
            board.setPos(getFromIndex(), pawn);

            break;

        case CASTLING_WHITE_LONG:
            CastlingMove.CASTLING_WHITE_LONG.undoSecond(board);
            break;
        case CASTLING_WHITE_SHORT:
            CastlingMove.CASTLING_WHITE_SHORT.undoSecond(board);
            break;
        case CASTLING_BLACK_SHORT:
            CastlingMove.CASTLING_BLACK_SHORT.undoSecond(board);
            break;
        case CASTLING_BLACK_LONG:
            CastlingMove.CASTLING_BLACK_LONG.undoSecond(board);
            break;
        }
    }

    @Override
    public byte getCapturedFigure() {
        return capturedFigure;
    }

    @Override
    public boolean isCapture() {
        return getCapturedFigure() != 0;
    }

    @Override
    public byte getFigureType() {
        return figureType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MoveImpl move = (MoveImpl) o;
        return figureType == move.figureType && fromIndex == move.fromIndex && toIndex == move.toIndex
                && enPassantOption == move.enPassantOption && enPassantCapturePos == move.enPassantCapturePos
                && capturedFigure == move.capturedFigure && type == move.type && promotedFigure == move.promotedFigure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureType, fromIndex, toIndex, enPassantOption, enPassantCapturePos, capturedFigure,
                promotedFigure, type);
    }

    public long toLongEncoded() {
        long l = (long) type |
                (long) figureType << 8 |
                (long) fromIndex << 16 |
                (long) toIndex << 24 |
                (long) enPassantOption << 32 |
                (long) enPassantCapturePos << 40 |
                (long) capturedFigure << 48 |
                (long) promotedFigure << 56;

        return l;
    }

    public static long longRepresentation(byte type, byte figureType, byte fromIndex, byte toIndex,
            byte enPassantOption, byte enPassantCapturePos,
            byte capturedFigure, byte promotedFigure) {
        long l = (long) type |
                (long) figureType << 8 |
                (long) fromIndex << 16 |
                (long) toIndex << 24 |
                (long) enPassantOption << 32 |
                (long) enPassantCapturePos << 40 |
                (long) capturedFigure << 48 |
                (long) promotedFigure << 56;

        return l;
    }

    public void fromLongEncoded(long l) {
        type = (byte) (l & 0xFFL);
        figureType = (byte) (l >>> 8 & 0xFFL);
        fromIndex = (byte) (l >>> 16 & 0xFFL);
        toIndex = (byte) (l >>> 24 & 0xFFL);
        enPassantOption = (byte) (l >>> 32 & 0xFFL);
        enPassantCapturePos = (byte) (l >>> 40 & 0xFFL);
        capturedFigure = (byte) (l >>> 48 & 0xFFL);
        promotedFigure = (byte) (l >>> 56 & 0xFFL);
    }

}
