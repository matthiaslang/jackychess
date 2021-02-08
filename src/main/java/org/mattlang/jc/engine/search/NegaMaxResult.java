package org.mattlang.jc.engine.search;

import java.util.List;

public class NegaMaxResult {
    public final int max;
    public final List<MoveScore> moveScores;

    public final PVList pvList;

    public final int targetDepth;

    public final int selDepth;

    public NegaMaxResult(int max, List<MoveScore> moveScores, PVList pvList, int targetDepth) {
        this.max = max;
        this.moveScores = moveScores;
        this.pvList = pvList;
        this.targetDepth=targetDepth;
        this.selDepth = targetDepth;

    }

    public NegaMaxResult(int max, List<MoveScore> moveScores, PVList pvList, int targetDepth, int selDepth) {
        this.max = max;
        this.moveScores = moveScores;
        this.pvList = pvList;
        this.targetDepth=targetDepth;
        this.selDepth = selDepth;

    }
}
