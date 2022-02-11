package org.mattlang.jc.engine.evaluation.parameval;

import java.util.HashMap;

import org.mattlang.jc.board.FigureType;

import lombok.Getter;

/**
 * A Description of Material used in Configurations.
 * It specifies Pieces and Pawns.
 *
 * Pieces must always fix specified.
 * Pawns can be omitted
 *
 * Example
 *
 * RB 0P  == one Rook, one Bishop, no Pawns
 *
 * RRB   == two Rooks, a Bishop, any number of Pawns since they are not specified
 *
 * RQ 2P == a rook, a queen and two pawns
 */
@Getter
public class MaterialDescription {

    private Material material;

    private boolean pawnsUnspecific = false;

    private boolean unspecified = false;

    public MaterialDescription(boolean unspecified) {
        this.unspecified = unspecified;
    }

    public MaterialDescription(Material material, boolean pawnsUnspecific) {
        this.material = material;
        this.pawnsUnspecific = pawnsUnspecific;
    }

    public static MaterialDescription parseDescr(String descr) {
        descr = descr.trim();
        if ("X".equals(descr)) {
            return new MaterialDescription(true);
        }
        String[] splitted = descr.split(" ");
        String pieces = splitted[0];
        String pawnstr = splitted.length >= 2 ? splitted[1] : "";

        int pieceMat = parsePieces(pieces);
        Integer pawns = parsePawns(pawnstr);
        boolean pawnsUnspecified = false;
        if (pawns == null) {
            pawns = 0;
            pawnsUnspecified = true;
        }
        return new MaterialDescription(new Material(pawns, pieceMat), pawnsUnspecified);
    }

    private static Integer parsePawns(String pawnstr) {
        if (pawnstr.length() > 2) {
            throw new ConfigParseException("Could not parse Pawn Description: " + pawnstr);
        }
        if (pawnstr.length() == 0) {
            return null;
        }
        if (pawnstr.length() == 1) {
            throw new ConfigParseException("Could not parse Pawn Description: " + pawnstr);
        }
        return Integer.parseInt(pawnstr.substring(0, 1));
    }

    private static int parsePieces(String pieces) {
        int val = 0;
        for (int i = 0; i < pieces.length(); i++) {
            val += pieceVal(pieces.charAt(i));
        }

        return val;
    }

    private static final HashMap<Character, Integer> PIECE_VALS = new HashMap<>();

    static {
        PIECE_VALS.put('0', 0);
        PIECE_VALS.put(FigureType.Knight.figureChar, 10);
        PIECE_VALS.put(FigureType.Bishop.figureChar, 100);
        PIECE_VALS.put(FigureType.Rook.figureChar, 1000);
        PIECE_VALS.put(FigureType.Queen.figureChar, 10000);

    }

    private static int pieceVal(char charAt) {
        if (!PIECE_VALS.containsKey(charAt)) {
            throw new ConfigParseException("Could not parse Piece Value Char: " + charAt);
        }
        return PIECE_VALS.get(charAt);
    }

    /**
     * Returns true, if this descriptions matches the other description.
     * This does not mean
     *
     * @param other
     * @return
     */
    public boolean matches(Material other) {
        if (unspecified) {
            return true;
        }
        if (material.getPieceMat() != other.getPieceMat()) {
            return false;
        }
        if (pawnsUnspecific) {
            return true;
        }
        return material.getPawns() == other.getPawns();
    }
}
