package org.mattlang.jc.engine.evaluation.parameval;

public class PawnCacheEntry {
    long zobrist;
    int score;
    long whitePassers;
    long blackPassers;

    public void reset() {
        zobrist = 0;
        score = 0;
        whitePassers = 0L;
        blackPassers = 0L;
    }
}
