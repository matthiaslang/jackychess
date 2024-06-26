package org.mattlang.jc.engine.search;

import java.util.List;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.moves.MoveImpl;

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

    /**
     * pv list from negamax search.
     */
    public final PVList pvList;

    /**
     * the target depth.
     */
    public final int targetDepth;

    /**
     * selected depth due to quiescence search.
     */
    public final int selDepth;

    public final int nodesVisited;
    public final int quiescenceNodesVisited;

    public NegaMaxResult(int directScore, List<Integer> pvMoves,
            SearchContext searchContext, int nodesVisited, int quiescenceNodesVisited) {
        this.directScore = directScore;
        this.max = searchContext.getSavedMoveScore();
        if (searchContext.getSavedMove() != 0) {
            this.savedMove = new MoveImpl(searchContext.getSavedMove());
        } else {
            this.savedMove = null;
        }
        this.pvList = new PVList(pvMoves);
        ;
        //        if (!pvList.getPvMoves().equals(pvMoves)){
        //             throw new IllegalStateException("hey here is something weird!")   ;
        //        }
        this.targetDepth = searchContext.getTargetDepth();
        this.selDepth = searchContext.getSelDepth();

        this.nodesVisited = nodesVisited;
        this.quiescenceNodesVisited = quiescenceNodesVisited;
    }

    @Override
    public String toString() {
        return "NegaMaxResult{" +
                "directScore=" + directScore +
                ", max=" + max +
                ", savedMove=" + (savedMove != null ? savedMove.toStr() : "") +
                ", pvList=" + pvList.toPvLogStr() +
                ", targetDepth=" + targetDepth +
                ", selDepth=" + selDepth +
                '}';
    }

    public String toLogString() {
        return "[" +
                "directScore=" + directScore +
                ", max=" + max +
                ", savedMove=" + (savedMove != null ? savedMove.toStr() : "") +
                ", pv=" + pvList.toPvLogStr() +
                ", depth=" + targetDepth + "/" + selDepth +
                ", nodes=" + nodesVisited + "/" + quiescenceNodesVisited +
                ']';
    }
}
