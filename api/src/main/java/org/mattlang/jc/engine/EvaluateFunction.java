package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.search.SearchThreadContextCache;

public interface EvaluateFunction {

    int eval(BoardRepresentation currBoard, Color who2Move);

    /**
     * Enables the evaluation function to keep a cache on thread level for their usage.
     * The evaluation function gets called once it is created with this method and
     * is then able to either create or reuse its own cache structure.
     * Its up to the evaluation function to make use of this.
     * @param cache
     */
    void associateThreadCache(SearchThreadContextCache cache);

}
