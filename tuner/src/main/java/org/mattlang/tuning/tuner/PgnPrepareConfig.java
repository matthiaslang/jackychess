package org.mattlang.tuning.tuner;

import lombok.Value;

@Value
public class PgnPrepareConfig {

    public static final PgnPrepareConfig DEFAULT = new PgnPrepareConfig(0, 0, 0, false);
    /**
     * to skip first n half moves. This is useful if in the pgn file the opening book moves are not marked as such.
     */
    private final int skipFirstNHalfMoves;

    private final int skipLastNHalfMoves;

    /**
     * do only add n half moves.
     */
    private final int addOnlyNHalfMoves;

    /**
     * write comments about the pgn source of the epd.
     */
    private final boolean writeComments;
}
