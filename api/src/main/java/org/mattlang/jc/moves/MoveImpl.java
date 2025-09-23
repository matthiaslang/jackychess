package org.mattlang.jc.moves;

import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.board.IndexConversion.parsePos;
import static org.mattlang.util.Assertions.*;

import java.util.Objects;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.Move;

import lombok.Getter;

/**
 * Represents a move on the board.
 * <p>
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 *
 * A move can be encoded in an int value:
 *
 * Encoding:
 *
 * type info:
 * 0 = normal
 * 1= promotion
 * 2=castling
 * 3= e.p
 * ==== 2 bit
 *
 * special info:
 * 3 bit == 8 promo infos, 4 castling infos... empty otherwise
 *
 * === 5 bit
 * figureType: 4 bit
 * fromIndex: 6 bit
 * toIndex: 6 bit
 * capturedFigure: 4 bit
 *
 * === 24 bit
 */
@Getter
public final class MoveImpl implements Move {

    public static final byte MASK_2 = 0b11;
    public static final byte SPECIAL_TYPE_MASK_3 = 0b11100;
    public static final byte MASK_4 = 0b1111;
    public static final byte MASK_5 = 0b11111;
    public static final byte MASK_6 = 0b111111;

    public static final int OFFSET_SPECIALTYPE = 2;
    public static final int OFFSET_FIGURETYPE = 5;
    public static final int OFFSET_CAPTUREDFIGURE = 21;
    public static final int OFFSET_TOINDEX = 15;
    public static final int OFFSET_FROMINDEX = 9;

    private byte figureType;

    private byte fromIndex;

    private byte toIndex;

    private byte capturedFigure;

    /**
     * type of move.
     * This encodes the type itself as well as some additional data for special types/moves.
     * - it encodes a the en passant follow up move info
     * - the respective castling
     * - the promotion
     */
    private byte type = NORMAL_MOVE;

    /*
     * Encoding:
     *
     * type info:
     * 0 = normal
     * 1= promotion
     * 2=castling
     * 3= e.p
     * ==== 2 bit
     *
     * special info:
     * 3 bit == 8 promo infos, 4 castling infos... empty otherwise
     *
     * === 5 bit
     * figureType: 4 bit
     * fromIndex: 6 bit
     * toIndex: 6 bit
     * capturedFigure: 4 bit
     *
     * === 24 bit
     *
     *
     * */

    /**
     * type info, contained in 2 bit.
     */
    public static final byte NORMAL_MOVE = 0;
    public static final byte PROMOTION_MOVE = 1;
    public static final byte CASTLING_MOVE = 2;
    public static final byte ENPASSANT_MOVE = 3;

    /**
     * extended info depending on type. 3 bit 8 values:
     */
    public static final byte PAWN_PROMOTION_W_KNIGHT = 0;
    public static final byte PAWN_PROMOTION_W_BISHOP = 1;
    public static final byte PAWN_PROMOTION_W_ROOK = 2;
    public static final byte PAWN_PROMOTION_W_QUEEN = 3;

    public static final byte PAWN_PROMOTION_B_KNIGHT = 4;
    public static final byte PAWN_PROMOTION_B_BISHOP = 5;
    public static final byte PAWN_PROMOTION_B_ROOK = 6;
    public static final byte PAWN_PROMOTION_B_QUEEN = 7;

    public static final byte CASTLING_WHITE_LONG = 0;
    public static final byte CASTLING_WHITE_SHORT = 1;
    public static final byte CASTLING_BLACK_SHORT = 2;
    public static final byte CASTLING_BLACK_LONG = 3;

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

    private static byte promotedFigureToType[] = new byte[Figure.B_Queen.figureCode + 1];

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

    public MoveImpl(int l) {
        fromLongEncoded(l);
    }

    // todo refactore this: this is not a valid initialized move! it contains only from/to!!
    // either delete this or create a helper class or something else...
    public MoveImpl(String moveStr) {
        fromIndex = parsePos(moveStr.substring(0, 2));
        toIndex = parsePos((moveStr.substring(2, 4)));
    }

    private MoveImpl(byte type, byte figureType, int from, int to, byte capturedFigure) {
        this.type = type;

        this.figureType = figureType;
        this.fromIndex = (byte) from;
        this.toIndex = (byte) to;
        this.capturedFigure = capturedFigure;

        if (BuildConstants.ASSERTIONS) {
            doAssertions();
        }
    }

    public MoveImpl(byte figureType, int from, int to, byte capturedFigure) {
        this(NORMAL_MOVE, figureType, from, to, capturedFigure);
    }

    private MoveImpl(CastlingMove castlingMove) {
        this.type = toType(CASTLING_MOVE, castlingMove.getType());
        this.figureType = FT_KING;
        this.fromIndex = castlingMove.getKingFrom();
        this.toIndex = castlingMove.getKingTo();

        if (BuildConstants.ASSERTIONS) {
            doAssertions();
        }
    }

    private static byte toType(byte basicType, byte specialType) {
        return (byte) (basicType & 0xff | (specialType << OFFSET_SPECIALTYPE));
    }

    public static MoveImpl createCastling(CastlingMove castlingMove) {
        return new MoveImpl(castlingMove);
    }

    public static MoveImpl createPromotion(int from, int to, byte capturedFigure, Figure promotedFigure) {
        return new MoveImpl(toType(PROMOTION_MOVE, promotedFigureToType[promotedFigure.figureCode]),
                FigureConstants.FT_PAWN,from, to, capturedFigure);
    }

    public static MoveImpl createEnPassant(int from, int to, byte capturedFigure) {
        return new MoveImpl(ENPASSANT_MOVE, FigureConstants.FT_PAWN, from, to, capturedFigure);
    }

    public final static int createNormalMove(byte figureType, int fromIndex, int toIndex, byte capturedFigure) {
        return longRepresentation(NORMAL_MOVE, figureType, (byte) fromIndex, (byte) toIndex,
                capturedFigure);
    }

    public final static int createCastlingMove(CastlingMove castlingMove) {
        return longRepresentation(toType(CASTLING_MOVE, castlingMove.getType()),
                FT_KING, castlingMove.getKingFrom(),
                castlingMove.getKingTo(),
                (byte) 0);
    }

    public final static int createPromotionMove(int from, int to, byte capturedFigure, Figure promotedFigure) {
        return longRepresentation(
                toType(PROMOTION_MOVE, promotedFigureToType[promotedFigure.figureCode]),
                FigureConstants.FT_PAWN, (byte) from, (byte) to,
                capturedFigure);
    }

    public final static int createEnPassantMove(int from, int to, byte capturedFigure) {
        return longRepresentation(ENPASSANT_MOVE, FigureConstants.FT_PAWN, (byte) from,
                (byte) to,
                capturedFigure);
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    @Override
    public String toStr() {
        return MoveToStringConverter.toStr(this);
    }

    @Override
    public String toUCIString(BoardRepresentation board) {
        return MoveToStringConverter.toUCIString(this, board);
    }

    @Override
    public boolean isEnPassant() {
        return getBasicType() == ENPASSANT_MOVE;
    }

    @Override
    public boolean isCastling() {
        return getBasicType() == CASTLING_MOVE;
    }

    @Override
    public boolean isPromotion() {
        return getBasicType() == PROMOTION_MOVE;
    }

    public boolean isQueenPromotion() {
        return isPromotion() && getSpecialType() == PAWN_PROMOTION_W_QUEEN
               || getSpecialType() == PAWN_PROMOTION_B_QUEEN;
    }

    public byte getSpecialType() {
        return (byte) ((type & SPECIAL_TYPE_MASK_3) >> OFFSET_SPECIALTYPE);
    }

    public byte getBasicType() {
        return (byte) (type & MASK_2);
    }

    public Figure getPromotedFigure() {
        return Figure.getFigureByCode(typeToPromotedFigure[getSpecialType()]);
    }

    public byte getPromotedFigureByte() {
        return typeToPromotedFigure[getSpecialType()];
    }

    @Override
    public int getMoveInt() {
        return toLongEncoded();
    }

    @Override
    public String toString() {
        return toStr();
    }

    @Override
    public byte getCastlingType() {
        return getSpecialType();
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

    public int toLongEncoded() {
        return (int) type & MASK_5 |
               (int) figureType << OFFSET_FIGURETYPE |
               (int) fromIndex << OFFSET_FROMINDEX |
               (int) toIndex << OFFSET_TOINDEX |
               (int) capturedFigure << OFFSET_CAPTUREDFIGURE;
    }

    public static int longRepresentation(byte type, byte figureType, byte fromIndex, byte toIndex,
            byte capturedFigure) {

        if (BuildConstants.ASSERTIONS) {
            assertFigureType(figureType);
            assertFieldNum(fromIndex);
            assertFieldNum(toIndex);
            assertFigureCodeOrEmpty(capturedFigure);
        }

        return (int) type & MASK_5 |
               (int) figureType << OFFSET_FIGURETYPE |
               (int) fromIndex << OFFSET_FROMINDEX |
               (int) toIndex << OFFSET_TOINDEX |
               (int) capturedFigure << OFFSET_CAPTUREDFIGURE;
    }

    public void fromLongEncoded(int l) {
        type = getType(l);
        figureType = getFigureType(l);
        fromIndex = getFromIndex(l);
        toIndex = getToIndex(l);

        capturedFigure = getCapturedFigure(l);

        if (BuildConstants.ASSERTIONS) {
            doAssertions();
        }
    }

    public static byte getCapturedFigure(int move) {
        return (byte) (move >>> OFFSET_CAPTUREDFIGURE & MASK_4);
    }

    public static byte getFigureType(int move) {
        return (byte) (move >>> OFFSET_FIGURETYPE & MASK_4);
    }

    public static byte getFromIndex(int move) {
        return (byte) (byte) (move >>> OFFSET_FROMINDEX & MASK_6);
    }

    public static byte getToIndex(int move) {
        return (byte) (byte) (move >>> OFFSET_TOINDEX & MASK_6);
    }

    public static Figure getPromotedFigure(int move) {
        return Figure.getFigureByCode(typeToPromotedFigure[getSpecialType(move)]);
    }

    public static boolean isCapture(int move) {
        return getCapturedFigure(move) != 0;
    }

    public static boolean isPromotion(int move) {
        return getBasicType(move) == PROMOTION_MOVE;
    }

    private static byte getType(int move) {
        return (byte) (move & MASK_5);
    }

    public static byte getBasicType(int move) {
        return (byte) (move & MASK_2);
    }

    public static byte getSpecialType(int move) {
        return (byte) ((move & SPECIAL_TYPE_MASK_3) >> OFFSET_SPECIALTYPE);
    }

    public static boolean isEnPassant(int move) {
        return getBasicType(move) == ENPASSANT_MOVE;
    }

    public static boolean isCastling(int move) {
        return getBasicType(move) == CASTLING_MOVE;
    }

    public static byte getCastlingType(int move) {
        return getSpecialType(move);
    }

    private void doAssertions() {

        assertionIsOneOf(type & MASK_2, NORMAL_MOVE, PROMOTION_MOVE, CASTLING_MOVE, ENPASSANT_MOVE);
        byte special = getSpecialType();
        switch (type & MASK_2) {
        case NORMAL_MOVE:
            assertion(special == 0, "special must be 0 for normal moves!");
            break;
        case PROMOTION_MOVE:
            assertion(special >= PAWN_PROMOTION_W_KNIGHT && special <= PAWN_PROMOTION_B_QUEEN,
                    "no valid special type for promotion!");
            break;
        case CASTLING_MOVE:
            assertion(special >= CASTLING_WHITE_LONG && special <= CASTLING_BLACK_LONG,
                    "no valid special type for castling!");
            break;
        case ENPASSANT_MOVE:
            assertion(special == 0, "special must be 0 for en passant moves!");
            break;
        }

        assertFigureType(figureType);
        assertFieldNum(fromIndex);
        assertFieldNum(toIndex);
        assertFigureCodeOrEmpty(capturedFigure);

        // conversion consistency
        int encoded = toLongEncoded();

        assertion(type == MoveImpl.getType(encoded), "conversion consistency failed!");
        assertion(figureType == MoveImpl.getFigureType(encoded), "conversion consistency failed!");
        assertion(fromIndex == MoveImpl.getFromIndex(encoded), "conversion consistency failed!");
        assertion(toIndex == MoveImpl.getToIndex(encoded), "conversion consistency failed!");
        assertion(capturedFigure == MoveImpl.getCapturedFigure(encoded), "conversion consistency failed!");
    }
}
