package org.mattlang.jc.material;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;
import lombok.Setter;

/**
 * Material of the board.
 * Encoded in a single int value.
 */
public class Material {

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

    private static final int[][] MAT_VALS = {
            // WHITE QQQRRRBBBNNNPPPP
            { W_PAWN_VAL, W_KNIGHT_VAL, W_BISHOP_VAL, W_ROOK_VAL, W_QUEEN_VAL },
            // BLACK QQQRRRBBBNNNPPPP
            { B_PAWN_VAL, B_KNIGHT_VAL, B_BISHOP_VAL, B_ROOK_VAL, B_QUEEN_VAL } };

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
        for (Color color : Color.values()) {
            for (FigureType type : FigureType.values()) {
                if (type != FigureType.King && type != FigureType.EMPTY) {
                    byte figureCode = type.figureCode;
                    material += Long.bitCount(bb.getPieceSet(figureCode, color))
                            * MAT_VALS[color.ordinal()][figureCode];
                }
            }
        }
    }

    public void subtract(byte fig) {
        int color = (fig & BLACK.code) == BLACK.code ? BLACK.ordinal() : WHITE.ordinal();
        int figIndex = (fig & MASK_OUT_COLOR);
        if (figIndex < FigureConstants.FT_KING) {
            material -= MAT_VALS[color][figIndex];
        }
    }

    public void add(byte fig) {
        int color = (fig & BLACK.code) == BLACK.code ? BLACK.ordinal() : WHITE.ordinal();
        int figIndex = (fig & MASK_OUT_COLOR);
        if (figIndex < FigureConstants.FT_KING) {
            material += MAT_VALS[color][figIndex];
        }
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
            if (figure.figureType != FigureType.King) {
                mat += MAT_VALS[figure.color.ordinal()][figure.figureType.figureCode];
            }
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

    public int getBlackMat() {
        return material & MASK_BLACK_PART;
    }
}
