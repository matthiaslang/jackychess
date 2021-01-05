package org.mattlang.jc.engine.search;

import java.util.List;

public class NegaMaxResult {
    public final int max;
    public final List<MoveScore> moveScores;

    public NegaMaxResult(int max, List<MoveScore> moveScores) {
        this.max = max;
        this.moveScores = moveScores;
    }
}
