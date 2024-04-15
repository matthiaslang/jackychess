package org.mattlang.jc.material;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import java.util.Arrays;
import java.util.Objects;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;
import lombok.Setter;

/**
 * Material of the board.
 * Encoded in a single int value.
 */
public class Material implements Comparable<Material> {

    public static final int W_PAWN_VAL = 1 << 0;
    public static final int W_KNIGHT_VAL = 1 << 4;
    public static final int W_BISHOP_VAL = 1 << 7;
    public static final int W_ROOK_VAL = 1 << 10;
    public static final int W_QUEEN_VAL = 1 << 13;
    public static final int B_PAWN_VAL = 1 << 16;
    public static final int B_KNIGHT_VAL = 1 << 20;
    public static final int B_BISHOP_VAL = 1 << 23;
    public static final int B_ROOK_VAL = 1 << 26;
    public static final int B_QUEEN_VAL = 1 << 29;

    private static final int[] MATS = new int[FigureConstants.MAX_FIGURE_INDEX];

    static {
        MATS[Figure.B_Pawn.figureCode] = B_PAWN_VAL;
        MATS[Figure.W_Pawn.figureCode] = W_PAWN_VAL;

        MATS[Figure.B_Knight.figureCode] = B_KNIGHT_VAL;
        MATS[Figure.W_Knight.figureCode] = W_KNIGHT_VAL;

        MATS[Figure.B_Bishop.figureCode] = B_BISHOP_VAL;
        MATS[Figure.W_Bishop.figureCode] = W_BISHOP_VAL;

        MATS[Figure.B_Rook.figureCode] = B_ROOK_VAL;
        MATS[Figure.W_Rook.figureCode] = W_ROOK_VAL;

        MATS[Figure.B_Queen.figureCode] = B_QUEEN_VAL;
        MATS[Figure.W_Queen.figureCode] = W_QUEEN_VAL;

        MATS[Figure.B_King.figureCode] = 0;
        MATS[Figure.W_King.figureCode] = 0;
    }

    private static final int MASK_OUT_PAWNS = 0b11111111111100001111111111110000;
    private static final int MASK_OUT_PIECES = 0b00000000000011110000000000001111;
    private static final int MASK_WHITE_PART = 0b00000000000000001111111111111111;
    private static final int MASK_BLACK_PART = 0b11111111111111110000000000000000;

    @Getter
    @Setter
    private int material;

    public Material() {
    }

    public Material(String spec) {
        this.material = parse(spec);
    }

    public Material(int material) {
        this.material = material;
    }

    public void init(BoardRepresentation board) {
        material = 0;
        BitChessBoard bb = board.getBoard();
        for (Figure figure : Figure.values()) {
            if (figure != Figure.EMPTY) {
                byte figureCode = figure.figureCode;
                material +=
                        Long.bitCount(bb.getPieceSet(figure.figureType.figureCode, figure.color)) * MATS[figureCode];
            }
        }
    }

    public void subtract(byte fig) {
        material -= MATS[fig];
    }

    public void add(byte fig) {
        material += MATS[fig];
    }

    public boolean isDrawByMaterial() {
        return material == Kk
                || material == KNk
                || material == Knk
                || material == Knnk
                || material == KNNk
                || material == KBk
                || material == Kbk
                || material == KNbk
                || material == KBnk
                || material == KBbk;

    }

    public static final int Kk = parse("Kk");
    public static final int KNk = parse("KNk");
    public static final int Knk = parse("Knk");
    public static final int Knnk = parse("Knnk");
    public static final int KNNk = parse("KNNk");
    public static final int KBk = parse("KBk");
    public static final int Kbk = parse("Kbk");
    public static final int KNbk = parse("KNbk");
    public static final int KBnk = parse("KBnk");
    public static final int KBbk = parse("KBbk");

    /**
     * Parse a material specification by Figure Characters. Lower characters == black figures, upper characters are
     * white figures.
     *
     * @param spec
     * @return
     */
    public static int parse(String spec) {
        int mat = 0;
        for (int i = 0; i < spec.length(); i++) {
            Figure figure = toFigureType(spec.charAt(i));
            mat += MATS[figure.figureCode];
        }
        return mat;
    }

    private static Figure toFigureType(char charAt) {
        return Figure.getFigureByCode(Figure.convertFigureChar(charAt));
    }

    public int getBlackAsWhitePart() {
        return material >>> 16;
    }

    public int getWhiteAsBlackPart() {
        return material << 16;
    }

    public int switchSidesOnMaterial() {
        return getWhiteAsBlackPart() | getBlackAsWhitePart();
    }

    /**
     * Return only the pieces material without the pawns.
     */
    public int getPieceMat() {
        return material & MASK_OUT_PAWNS;
    }

    public int getPawnsMat() {
        return material & MASK_OUT_PIECES;
    }

    public int getWhiteMat() {
        return material & MASK_WHITE_PART;
    }

    public int getWhitePieceMat() {
        return material & MASK_WHITE_PART & MASK_OUT_PAWNS;
    }

    public int getBlackMat() {
        return material & MASK_BLACK_PART;
    }

    public int getBlackPieceMat() {
        return material & MASK_BLACK_PART & MASK_OUT_PAWNS;
    }

    public boolean hasMoreWhiteMat(int thanThisWhiteMat) {
        int meWhite = getWhiteMat();
        // in case the other has only  a king and we some material:
        if (meWhite > 0 && thanThisWhiteMat == 0) {
            return true;
        }
        return (thanThisWhiteMat & meWhite) == thanThisWhiteMat && meWhite > thanThisWhiteMat;
    }

    /**
     * Return true, if the given (white) material has more material than the other matieral.
     * Measured in figures: Means this material has the same figures as the other material but also more figures.
     *
     * @param thanThisWhiteMaterial
     * @return
     */
    public boolean hasMoreWhiteMat(Material thanThisWhiteMaterial) {
        return hasMoreWhiteMat(thanThisWhiteMaterial.getWhiteMat());
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        int mat = material;

        for (Figure figure : Arrays.asList(Figure.B_Queen, Figure.B_Rook, Figure.B_Bishop, Figure.B_Knight,
                Figure.B_Pawn,
                Figure.W_Queen, Figure.W_Rook, Figure.W_Bishop, Figure.W_Knight, Figure.W_Pawn)) {

            int figVal = MATS[figure.figureCode];
            while (mat >= figVal) {
                b.append(figure.figureChar);
                mat -= figVal;
            }

        }

        return b.toString();
    }

    public boolean hasNonPawnMat(Color color) {
        int nonPawnMatOfColor = material & MASK_OUT_PAWNS & (color == WHITE ? MASK_WHITE_PART : MASK_BLACK_PART);
        return nonPawnMatOfColor > 0;
    }

    public boolean hasNonPawnMat(int color) {
        int nonPawnMatOfColor = material & MASK_OUT_PAWNS & (color == nWhite ? MASK_WHITE_PART : MASK_BLACK_PART);
        return nonPawnMatOfColor > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Material material1 = (Material) o;
        return material == material1.material;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material);
    }

    @Override
    public int compareTo(Material o) {
        return material - o.material;
    }
}
