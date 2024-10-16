package org.mattlang.jc.engine.evaluation.parameval;

import java.util.Arrays;

public class PawnCacheEntry {

    long zobrist;
    int score;
    long whitePassers;
    long blackPassers;

    int[] pksafety = new int[2];
    int pkeval;

    public void reset() {
        zobrist = 0;
        score = 0;
        whitePassers = 0L;
        blackPassers = 0L;
        pkeval = 0;
        Arrays.fill(pksafety, 0);
    }
}
