package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Material of one Side: a int holding as decimal the material of one color.
 *
 * 1*pawn + 10*knight + 100*bishop + 1000*rook + 10000*queen.
 *
 * This is used to compare patterns of small material constellations.

 */
@AllArgsConstructor
@Getter
public class Material {

    private int pawns;

    private int pieceMat;

    /**
     * Decodes to a int holding as decimal the material of one color.
     *
     * 1*pawn + 10*knight + 100*bishop + 1000*rook + 10000*queen.
     *
     * This is used to compare patterns of small material constellations.
     *
     * @param bb
     * @param color
     * @return
     */
    public static Material fromBoard(BitChessBoard bb, int color) {
        int pawns = bb.getPawnsCount(color);
        int knights = bb.getKnightsCount(color);
        int bishops = bb.getBishopsCount(color);
        int rooks = bb.getRooksCount(color);
        int queens = bb.getQueensCount(color);

        int pieceMat = 10 * knights + 100 * bishops + 1000 * rooks + 10000 * queens;
        return new Material(pawns, pieceMat);
    }
}
