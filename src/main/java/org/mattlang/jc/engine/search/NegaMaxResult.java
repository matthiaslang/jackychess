package org.mattlang.jc.engine.search;

import java.util.List;

import org.mattlang.jc.board.Move;

public class NegaMaxResult {

    /**
     * direct score result from negamax. (could be theoretically different to max).
     */
    public final int directScore;
    /**
     * max score from negamax.
     */
    public final int max;
    /**
     * Best move from negamax.
     */
    public final Move savedMove;

    /***
     * Scores for (all?) moves on ply 1.
     */
    public final List<MoveScore> moveScores;

    /** pv list from negamax search. */
    public final PVList pvList;

    /** the target depth. */
    public final int targetDepth;

    /** selected depth due to quiescence search. */
    public final int selDepth;

    public NegaMaxResult(int directScore, int max, Move savedMove, List<MoveScore> moveScores, List<Integer> pvMoves,
            int targetDepth,
            int selDepth) {
        this.directScore = directScore;
        this.max = max;
        this.savedMove = savedMove;
        this.moveScores = moveScores;
        this.pvList = new PVList(pvMoves);
        ;
        //        if (!pvList.getPvMoves().equals(pvMoves)){
        //             throw new IllegalStateException("hey here is something weird!")   ;
        //        }
        this.targetDepth = targetDepth;
        this.selDepth = selDepth;

    }

    @Override
    public String toString() {
        return "NegaMaxResult{" +
                "directScore=" + directScore +
                ", max=" + max +
                ", savedMove=" + (savedMove != null ? savedMove.toStr() : "") +
                ", moveScores=" + moveScores +
                ", pvList=" + pvList.toPvStr() +
                ", targetDepth=" + targetDepth +
                ", selDepth=" + selDepth +
                '}';
    }
}
