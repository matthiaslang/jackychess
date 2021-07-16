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
public final class MoveImpl implements Move {

    public static final int NOT_SORTED = Integer.MAX_VALUE;

    private byte figureType;

    private byte fromIndex;

    private byte toIndex;

    private byte capturedFigure;

    /**
     * type of move.
     * This encodes the type itself as well as some additional data for special types/moves.
     *  - it encodes a the en passant follow up move info
     *  - the respective castling
     *  - the promotion
     */
    private byte type = NORMAL_MOVE;

    public static final byte NORMAL_MOVE = 1;

    public static final byte PAWN_PROMOTION_W_KNIGHT = 2;
    public static final byte PAWN_PROMOTION_W_BISHOP = 3;
    public static final byte PAWN_PROMOTION_W_ROOK = 4;
    public static final byte PAWN_PROMOTION_W_QUEEN = 5;

    public static final byte PAWN_PROMOTION_B_KNIGHT = 6;
    public static final byte PAWN_PROMOTION_B_BISHOP = 7;
    public static final byte PAWN_PROMOTION_B_ROOK = 8;
    public static final byte PAWN_PROMOTION_B_QUEEN = 9;

    public static final byte CASTLING_WHITE_LONG = 10;
    public static final byte CASTLING_WHITE_SHORT = 11;
    public static final byte CASTLING_BLACK_SHORT = 12;
    public static final byte CASTLING_BLACK_LONG = 13;
    public static final byte ENPASSANT_MOVE = 14;

    private int order = NOT_SORTED;

    private static byte typeToPromotedFigure[] = new byte[PAWN_PROMOTION_B_QUEEN + 1];

    static {
        typeToPromotedFigure[PAWN_PROMOTION_W_KNIGHT] = Figure.W_Knight.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_W_BISHOP] = Figure.W_Bishop.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_W_ROOK] = Figure.W_Rook.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_W_QUEEN] = Figure.W_Queen.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_B_KNIGHT] = Figure.B_Knight.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_B_BISHOP] = Figure.B_Bishop.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_B_ROOK] = Figure.B_Rook.figureCode;
        typeToPromotedFigure[PAWN_PROMOTION_B_QUEEN] = Figure.B_Queen.figureCode;
    }

    private static byte promotedFigureToType[] = new byte[127];

    static {
        promotedFigureToType[Figure.W_Knight.figureCode] = PAWN_PROMOTION_W_KNIGHT;
        promotedFigureToType[Figure.W_Bishop.figureCode] = PAWN_PROMOTION_W_BISHOP;
        promotedFigureToType[Figure.W_Rook.figureCode] = PAWN_PROMOTION_W_ROOK;
        promotedFigureToType[Figure.W_Queen.figureCode] = PAWN_PROMOTION_W_QUEEN;

        promotedFigureToType[Figure.B_Knight.figureCode] = PAWN_PROMOTION_B_KNIGHT;
        promotedFigureToType[Figure.B_Bishop.figureCode] = PAWN_PROMOTION_B_BISHOP;
        promotedFigureToType[Figure.B_Rook.figureCode] = PAWN_PROMOTION_B_ROOK;
        promotedFigureToType[Figure.B_Queen.figureCode] = PAWN_PROMOTION_B_QUEEN;
    }

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

    public MoveImpl(byte type, byte figureType, int from, int to, byte capturedFigure) {
        this.type = type;
        this.figureType = figureType;
        this.fromIndex = (byte) from;
        this.toIndex = (byte) to;
        this.capturedFigure = capturedFigure;
    }

    private MoveImpl(int from, int to, byte capturedFigure, Figure promotedFigure) {
        this(FigureConstants.FT_PAWN, from, to, capturedFigure);
        this.type = promotedFigureToType[promotedFigure.figureCode];
    }

    private MoveImpl(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        this(FigureConstants.FT_PAWN, from, to, capturedFigure);
        this.type = (byte) (ENPASSANT_MOVE + enPassantCapturePos);
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

    public final static MoveImpl createNormal(byte figureType, int fromIndex, int toIndex, byte capturedFigure
    ) {
        return new MoveImpl(NORMAL_MOVE, figureType, (byte)fromIndex, (byte)toIndex, capturedFigure);
    }

    public final static long createNormalMove(byte figureType, int fromIndex, int toIndex, byte capturedFigure) {
        return longRepresentation(NORMAL_MOVE, figureType, (byte) fromIndex, (byte) toIndex,
                capturedFigure);
    }

    public final static long createCastlingMove(CastlingMove castlingMove) {
        return longRepresentation(castlingMove.getType(), (byte) 0, castlingMove.getFromIndex(),
                castlingMove.getToIndex(),
                (byte) 0);
    }

    public final static long createPromotionMove(int from, int to, byte capturedFigure, Figure promotedFigure) {
        return longRepresentation(promotedFigureToType[promotedFigure.figureCode],
                FigureConstants.FT_PAWN, (byte) from, (byte) to,
                capturedFigure);
    }

    public final static long createEnPassantMove(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        return longRepresentation((byte) (ENPASSANT_MOVE + enPassantCapturePos), FigureConstants.FT_PAWN, (byte) from,
                (byte) to,
                capturedFigure);
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
    public boolean isEnPassant() {
        return type >= ENPASSANT_MOVE;
    }

    @Override
    public boolean isCastling() {
        return type >= CASTLING_WHITE_LONG && type <=CASTLING_BLACK_LONG;
    }

    @Override
    public boolean isPromotion() {
        return type >= PAWN_PROMOTION_W_KNIGHT && type <= PAWN_PROMOTION_B_QUEEN;
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

    private int decodeEnPassantCapturePos(){
        return type - ENPASSANT_MOVE;
    }

    @Override
    public void move(BoardRepresentation board) {
        board.move(getFromIndex(), getToIndex());

        if (type >= ENPASSANT_MOVE) {
            board.setPos(decodeEnPassantCapturePos(), FigureConstants.FT_EMPTY);
        } else if (isPromotion()) {
            board.setPos(getToIndex(), typeToPromotedFigure[type]);
        } else {
            switch (type) {
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
    }

    @Override
    public void undo(BoardRepresentation board) {
        board.move(getToIndex(), getFromIndex());
        if (capturedFigure != 0) {
            board.setPos(getToIndex(), capturedFigure);
        }
        if (type >= ENPASSANT_MOVE) {
            // override the "default" overrider field with empty..
            board.setPos(getToIndex(), FigureConstants.FT_EMPTY);
            // because we have the special en passant capture pos which we need to reset with the captured figure
            board.setPos(decodeEnPassantCapturePos(), getCapturedFigure());
        } else if (isPromotion()) {
            Figure promotedFigure = board.getFigure(getFromIndex());
            Figure pawn = promotedFigure.color == Color.WHITE ? Figure.W_Pawn : Figure.B_Pawn;
            board.setPos(getFromIndex(), pawn);
        } else {
            switch (type) {
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
                && capturedFigure == move.capturedFigure && type == move.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureType, fromIndex, toIndex, capturedFigure, type);
    }

    public long toLongEncoded() {
        long l = (long) type |
                (long) figureType << 8 |
                (long) fromIndex << 16 |
                (long) toIndex << 24 |
                (long) capturedFigure << 32;

        return l;
    }

    public static long longRepresentation(byte type, byte figureType, byte fromIndex, byte toIndex,
            byte capturedFigure) {
        return (long) type |
                (long) figureType << 8 |
                (long) fromIndex << 16 |
                (long) toIndex << 24 |
                (long) capturedFigure << 32;
    }

    public void fromLongEncoded(long l) {
        type = (byte) (l & 0xFF);
        figureType = (byte) (l >>> 8 & 0b1111111);
        fromIndex = (byte) (l >>> 16 & 0b1111111);
        toIndex = (byte) (l >>> 24 & 0b1111111);

        capturedFigure = (byte) (l >>> 32 & 0b1111111);
    }

}
