package org.mattlang.jc.engine.tt;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface TTCacheInterface extends StatisticsCollector {

    /**
     * Searches the TT cache for a hit for the board zobrist key.
     * If matched, it updates the result into the result object.
     *
     * @param result
     * @param board
     */
    boolean findEntry(TTResult result, BoardRepresentation board);

    void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta,
            int depth, int move);

    void updateAging(BoardRepresentation board);

    long calcHashFull();

    /**
     * Is this Cache "thread safe" and usable within a lazy smp search?
     *
     * @return
     */
    default boolean isUsableForLazySmp() {
        return false;
    }

    void reset();
}
