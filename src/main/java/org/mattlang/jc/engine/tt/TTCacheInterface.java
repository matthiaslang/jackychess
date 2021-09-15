package org.mattlang.jc.engine.tt;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface TTCacheInterface extends StatisticsCollector {

    TTEntry getTTEntry(BoardRepresentation board, Color side);

    void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta,
            int depth, int move);

    void updateAging(BoardRepresentation board);

    long calcHashFull();
}
