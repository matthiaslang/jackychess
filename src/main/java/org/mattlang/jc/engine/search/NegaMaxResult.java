package org.mattlang.jc.engine.search;

import java.util.List;

import org.mattlang.jc.board.Move;

public class NegaMaxResult {
    public final int max;
    public final List<MoveScore> moveScores;

    public final PVList pvList;

    public final int targetDepth;

    public final int selDepth;


    public NegaMaxResult(int max, List<MoveScore> moveScores, List<Move> pvMoves,int targetDepth, int selDepth) {
        this.max = max;
        this.moveScores = moveScores;
        this.pvList = new PVList(pvMoves);;
//        if (!pvList.getPvMoves().equals(pvMoves)){
//             throw new IllegalStateException("hey here is something weird!")   ;
//        }
        this.targetDepth=targetDepth;
        this.selDepth = selDepth;

    }
}
