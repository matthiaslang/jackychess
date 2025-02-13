package org.mattlang.jc;

public class Constants {

    /**
     * Default cache size if not set via uci.
     */
    public static final int DEFAULT_CACHE_SIZE_MB = 128;

    /**
     * max moves on a position, used for static structure allocation.
     */
    public static final int MAX_MOVES = 256;

    /**
     * max ply used for parameters.
     */
    public static final int MAX_PLY = 64;

    /**
     * Maximum supported search depth.
     */
    public static final int MAX_DEPTH = MAX_PLY - 1;

    /**
     * our minimum search depth is 3, since otherwise we come into some technical issues with indexing.
     * Because of that we do not support lower depths, since this makes anyway not really sense.
     */
    public static final int MIN_DEPTH = 3;

    /**
     * maximum Threads supported.
     */
    public static final int MAX_THREADS = 8;

    /**
     * max ply used to setup array structures.
     */
    public static final int MAX_PLY_INDEX = MAX_PLY + MAX_PLY;

    /**
     * number of chess board fields.
     */
    public static final int NUM_BOARD_FIELDS = 64;

}
