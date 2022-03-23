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

    private static final int[][] MAT_VALS = {
            // WHITE QQQRRRBBBNNNPPPP
            { 1 << 0, 1 << 4, 1 << 7, 1 << 10, 1 << 13 },
            // BLACK QQQRRRBBBNNNPPPP
            { 1 << 16, 1 << 20, 1 << 23, 1 << 26, 1 << 29 } };

    @Getter
    @Setter
    private int material;

    public void init(BoardRepresentation board) {
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
}
