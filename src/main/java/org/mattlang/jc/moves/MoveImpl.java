package org.mattlang.jc.moves;

import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.board.IndexConversion.convert;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import java.util.Objects;

import org.mattlang.jc.board.*;

import lombok.Getter;

/**
 * Represents a move on the board.
 * <p>
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 *
 * A move can be encoded in an int value:
 *
 * type: 0-14: 7 bits : contains type, but also promotion figure, en passant capture pos
 * figureType: 5 bits
 * fromINdex: 7 bits
 * toIndex: 7 bits
 * capturedFigure: 5 bits
 * == 31 bits
 * 
 */
@Getter
public final class MoveImpl implements Move {

    public static final byte MASK_4 = 0b1111;
    public static final byte MASK_5 = 0b11111;
    public static final byte MASK_7 = 0b1111111;

    public static final int NOT_SORTED = Integer.MAX_VALUE;
    public static final int OFFSET_FIGURETYPE = 7;
    public static final int OFFSET_CAPTUREDFIGURE = 26;
    public static final int OFFSET_TOINDEX = 19;
    public static final int OFFSET_FROMINDEX = 12;

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

    public MoveImpl(int l) {
        fromLongEncoded(l);
    }

    // todo refactore this: this is not a valid initialized move! it contains only from/to!!
    // either delete this or create a helper class or something else...
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
        this.figureType = FT_KING;
        this.type = castlingMove.getType();
        this.fromIndex = castlingMove.getKingFrom();
        this.toIndex = castlingMove.getKingTo();
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

    public final static int createNormalMove(byte figureType, int fromIndex, int toIndex, byte capturedFigure) {
        return longRepresentation(NORMAL_MOVE, figureType, (byte) fromIndex, (byte) toIndex,
                capturedFigure);
    }

    public final static int createCastlingMove(CastlingMove castlingMove) {
        return longRepresentation(castlingMove.getType(), FT_KING, castlingMove.getKingFrom(),
                castlingMove.getKingTo(),
                (byte) 0);
    }

    public final static int createPromotionMove(int from, int to, byte capturedFigure, Figure promotedFigure) {
        return longRepresentation(promotedFigureToType[promotedFigure.figureCode],
                FigureConstants.FT_PAWN, (byte) from, (byte) to,
                capturedFigure);
    }

    public final static int createEnPassantMove(int from, int to, byte capturedFigure, int enPassantCapturePos) {
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

    @Override
    public String toStr() {
        String coords = convert(fromIndex) + convert(toIndex);
        if (isPromotion()) {
            char figureChar = Character.toLowerCase(getPromotedFigure().figureChar);
            coords += figureChar;
        }
        return coords;
    }

    @Override
    public String toUCIString(BoardRepresentation board) {
        String coords = convert(fromIndex) + convert(toIndex);
        if (isPromotion()) {
            char figureChar = Character.toLowerCase(getPromotedFigure().figureChar);
            coords += figureChar;
        }
        // in chess960 we code a castling move as king captures rook to make it distinct.
        // otherwise in some chess960 postitions it could not be distinguished between a normal king move.
        if (board.isChess960() && isCastling()) {
            CastlingMove castlingMove = board.getBoardCastlings().getCastlingMove(getCastlingType());
            coords = convert(castlingMove.getKingFrom()) + convert(castlingMove.getRookFrom());
        }

        return coords;
    }

    /**
     * Returns a long algebraic like representation. Maybe not complete to standard... as it is currently only
     * used for debugging
     *
     * @return
     */
    public String toLongAlgebraic() {
        if (isCastling()) {
            if (type == CASTLING_WHITE_LONG || type == CASTLING_BLACK_LONG) {
                return "O-O-O";
            } else {
                return "O-O";
            }
        }
        String coords;
        if (isCapture()) {
            coords = convert(fromIndex) + "x" + convert(toIndex);
        } else {
            coords = convert(fromIndex) + "-" + convert(toIndex);
        }
        for (FigureType value : FigureType.values()) {
            if (getFigureType() == value.figureCode && getFigureType() != FigureType.Pawn.figureCode) {
                coords = Character.toUpperCase(value.figureChar) + coords;

            }
        }
        if (isPromotion()) {
            char figureChar = Character.toLowerCase(getPromotedFigure().figureChar);
            coords += figureChar;
        }
        if (isEnPassant()) {
            coords += " e.p";
        }

        return coords;

    }

    @Override
    public boolean isEnPassant() {
        return type >= ENPASSANT_MOVE;
    }

    @Override
    public boolean isCastling() {
        return type >= CASTLING_WHITE_LONG && type <= CASTLING_BLACK_LONG;
    }

    @Override
    public boolean isPromotion() {
        return type >= PAWN_PROMOTION_W_KNIGHT && type <= PAWN_PROMOTION_B_QUEEN;
    }

    public boolean isQueenPromotion() {
        return type == PAWN_PROMOTION_W_QUEEN || type == PAWN_PROMOTION_B_QUEEN;
    }

    public Figure getPromotedFigure() {
        return Figure.getFigureByCode(typeToPromotedFigure[type]);
    }

    public byte getPromotedFigureByte() {
        return typeToPromotedFigure[type];
    }

    @Override
    public int getMoveInt() {
        return toLongEncoded();
    }

    @Override
    public int getEnPassantCapturePos() {
        return decodeEnPassantCapturePos();
    }

    @Override
    public String toString() {
        return toStr();
    }

    private int decodeEnPassantCapturePos() {
        return type - ENPASSANT_MOVE;
    }

    @Override
    public byte getCastlingType() {
        return type;
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
        int l = (int) type & MASK_7 |
                (int) figureType << OFFSET_FIGURETYPE |
                (int) fromIndex << OFFSET_FROMINDEX |
                (int) toIndex << OFFSET_TOINDEX |
                (int) capturedFigure << OFFSET_CAPTUREDFIGURE;

        return l;
    }

    public static int longRepresentation(byte type, byte figureType, byte fromIndex, byte toIndex,
            byte capturedFigure) {
        return (int) type & MASK_7 |
                (int) figureType << OFFSET_FIGURETYPE |
                (int) fromIndex << OFFSET_FROMINDEX |
                (int) toIndex << OFFSET_TOINDEX |
                (int) capturedFigure << OFFSET_CAPTUREDFIGURE;
    }

    public void fromLongEncoded(int l) {
        type = (byte) (l & MASK_7);
        figureType = (byte) (l >>> OFFSET_FIGURETYPE & MASK_5);
        fromIndex = (byte) (l >>> OFFSET_FROMINDEX & MASK_7);
        toIndex = (byte) (l >>> OFFSET_TOINDEX & MASK_7);

        capturedFigure = (byte) (l >>> OFFSET_CAPTUREDFIGURE & MASK_5);
    }

    public static byte getCapturedFigure(int move) {
        return (byte) (move >>> 26 & MASK_5);
    }

    public static byte getFigureType(int move){
        return (byte) (move >>> OFFSET_FIGURETYPE & MASK_5);
    }

    public static byte getFromIndex(int move){
        return (byte)  (byte) (move >>> OFFSET_FROMINDEX & MASK_7);
    }

    public static byte getToIndex(int move){
        return (byte)  (byte) (move >>> OFFSET_TOINDEX & MASK_7);
    }

    public static boolean isCapture(int move) {
        return getCapturedFigure(move) != 0;
    }
}
