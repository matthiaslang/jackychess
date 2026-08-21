package org.mattlang.jc.engine.search;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Move;

import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;

public record IterativeRoundResult(NegaMaxResult rslt, StopWatch roundWatch) {

    public boolean isCheckMate() {
        return Math.abs(Math.abs(rslt.directScore) - KING_WEIGHT) < 100;
    }

    public boolean hasResults() {
        return rslt != null;
    }

    public Move getOptionalBestMove() {
        return rslt != null ? rslt.savedMove : null;
    }
}
