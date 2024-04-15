package org.mattlang.jc.engine.evaluation.parameval;

import static java.util.Objects.requireNonNull;

import org.mattlang.jc.material.Material;

import lombok.Getter;

/**
 * A Description of Material used in Configurations.
 * It specifies Pieces and Pawns.
 *
 * Pieces must be fix specified but can be combined with + or *.
 * Pawns can be omitted
 *
 * + == more than the defined material
 * * == more or equal than the defined material
 *
 * Example
 *
 * RB 0P  == one Rook, one Bishop, no Pawns
 *
 * RRB   == two Rooks, a Bishop, any number of Pawns since they are not specified
 *
 * RQ 2P == a rook, a queen and two pawns
 *
 * RQ+ == a rook, a queen and some more unspecified material
 * RB* == a rook, a bishop at least and maybe even more unspecified material
 *
 * X == any material. This makes only sense in certain situations like describing a weaker material side when a stronger
 * material description is given which would mean that it is anything but at least weaker then the stronger side
 */
@Getter
public class MaterialDescription {

    /**
     * Material of the description of one color (coded as white only in the material, black==0).
     */
    private Material material;

    private MaterialComparison comparison = MaterialComparison.EQUAL;

    public MaterialDescription(MaterialComparison comparison) {
        this.comparison = requireNonNull(comparison);
    }

    public MaterialDescription(Material material, MaterialComparison comparison) {
        this.material = requireNonNull(material);
        this.comparison = requireNonNull(comparison);
    }

    public static MaterialDescription parseDescr(String descr) {
        descr = descr.trim();
        if ("X".equals(descr)) {
            return new MaterialDescription(MaterialComparison.UNSPECIFIED);
        }

        String matDescr = descr.toUpperCase();

        MaterialComparison comparison = MaterialComparison.EQUAL;

        if (matDescr.contains("+")) {
            comparison = MaterialComparison.MORE;
        }
        if (matDescr.contains("*")) {
            comparison = MaterialComparison.MORE_OR_EQUAL;
        }
        matDescr = matDescr.replace("+", "");
        matDescr = matDescr.replace("*", "");

        return new MaterialDescription(new Material(matDescr), comparison);
    }

    /**
     * Returns true, if this descriptions matches the other description.
     * This does not mean
     *
     * @param other
     * @return
     */
    public boolean matches(Material other) {
        switch (comparison) {
        case UNSPECIFIED:
            return true;
        case EQUAL:
            return compareEqual(other);
        case MORE:
            return compareMore(other);
        case MORE_OR_EQUAL:
            return compareEqual(other) || compareMore(other);
        default:
            throw new IllegalStateException("unkown Comparison value: " + comparison);
        }

    }

    private boolean compareMore(Material other) {
        return other.hasMoreWhiteMat(material);
    }

    private boolean compareEqual(Material other) {

        if (material.getPieceMat() != other.getPieceMat()) {
            return false;
        }
        return material.getPawnsMat() == other.getPawnsMat();
    }

    public String toString(){
        return material.toString() + " " + comparison.getSymbol();
    }
}
