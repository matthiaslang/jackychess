package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

public class PattChecker2 {

    private PattStatsCollector statsgenerator = new PattStatsCollector();

    public final int eval(BoardRepresentation currBoard, Color who2Move) {

        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);

        int pattCheck = pattCheck(wstats, bstats, who2Move);
        return pattCheck;

    }


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
