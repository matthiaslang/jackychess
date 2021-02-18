package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class PattChecking {

    /**
     * Static method to check if patt is for the current side to move. This is mainly used as "api" for clients,
     * not internal in the search algorithms, since it is not so fast as it creates internal board stats itself.
     * @param board
     * @param who2Move
     * @return
     */
    public static boolean isPatt(BoardRepresentation board, Color who2Move) {
        BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

        BoardStats wstats = statsgenerator.gen(board, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(board, Color.BLACK);

        int pattCheck = PattChecking.pattCheck(wstats, bstats, who2Move);
        return pattCheck == -PATT_WEIGHT || pattCheck == PATT_WEIGHT;
    }

    /**
     * Patt check used inside search algorithm.
     * @param wstats
     * @param bstats
     * @param who2Move
     * @return
     */
    public static final int pattCheck(BoardStats wstats, BoardStats bstats, Color who2Move) {

        int who2mov = who2Move == Color.WHITE ? 1 : -1;


        int whiteMakesPatt = isPatt(wstats, bstats);
        int blackMakesPatt = isPatt(bstats, wstats);

        if (whiteMakesPatt == 1 || blackMakesPatt == 1) {
            // patt dominates the score and resets all previous score calculation:
            return -PATT_WEIGHT * (whiteMakesPatt - blackMakesPatt) * who2mov;
        }
        return 0;
    }

    private static final int isPatt(BoardStats me, BoardStats other) {
        // if only king of other side can move, we need to take care and further analyse:
        if (Long.bitCount(other.kingMobilityBitBoard) == other.mobility) {
            // diff our mobility/captures possibility with the enemies king mobility:
            long possibleCaptures = me.mobilityBitBoard | me.hypotheticalPawnCaptures;
            if ((possibleCaptures & other.kingMobilityBitBoard) == other.kingMobilityBitBoard) {
                // big penalty for patt!
                // todo or make additional evaluations based on material if we should do accept the patt?
                return 1;
            }
        }
        return 0;
    }

}
