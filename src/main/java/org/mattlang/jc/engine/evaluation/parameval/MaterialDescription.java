package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.material.Material;

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

    /**
     * Material of the description of one color (coded as white only in the material, black==0).
     */
    private Material material;

    private boolean pawnsUnspecific = false;

    /* complete unspecified. this makes only sense for the weaker side: means it is unspecified, but anyway lower/weaker than the stronger side. */
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

        String matDescr = pieces.toUpperCase();

        boolean pawnsUnspecified = false;
        if (pawnstr.trim().length() == 0) {
            pawnsUnspecified = true;
        } else {
            matDescr += convertPawnStr(pawnstr);
        }

        return new MaterialDescription(new Material(matDescr), pawnsUnspecified);
    }

    private static String convertPawnStr(String pawnstr) {
        if (pawnstr.length() > 2) {
            throw new ConfigParseException("Could not parse Pawn Description: " + pawnstr);
        }

        if (pawnstr.length() == 1) {
            throw new ConfigParseException("Could not parse Pawn Description: " + pawnstr);
        }
        int numPawns = Integer.parseInt(pawnstr.substring(0, 1));
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < numPawns; i++) {
            b.append("P");
        }
        return b.toString();
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
        return material.getPawnsMat() == other.getPawnsMat();
    }
}
