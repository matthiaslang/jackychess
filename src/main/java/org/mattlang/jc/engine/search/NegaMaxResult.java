package org.mattlang.jc.engine.search;

import java.util.List;

public class NegaMaxResult {
    public final int max;
    public final List<MoveScore> moveScores;

    public final PVList pvList;

    public NegaMaxResult(int max, List<MoveScore> moveScores, PVList pvList) {
        this.max = max;
        this.moveScores = moveScores;
        this.pvList = pvList;
    }
}
