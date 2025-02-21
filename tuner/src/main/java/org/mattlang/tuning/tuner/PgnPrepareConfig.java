package org.mattlang.tuning.tuner;

import lombok.Value;

@Value
public class PgnPrepareConfig {

    public static final PgnPrepareConfig DEFAULT = new PgnPrepareConfig(0, 0);
    /**
     * to skip first n half moves. This is useful if in the pgn file the opening book moves are not marked as such.
     */
    private final int skipFirstNHalfMoves;

    private final int skipLastNHalfMoves;
}
