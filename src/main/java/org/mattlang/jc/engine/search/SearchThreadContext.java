package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.tt.IntCache;
import org.mattlang.jc.uci.GameContext;

import lombok.Getter;

/**
 * Holds variables used during a search for one thread which is used for all nested iterative deepening and negamax
 * search.
 * This includes movelist and all heuristical data like killer heuristic, history heuristic, pv cache, etc.
 *
 * All structures which are needed by one search thread should be refactored in this class finally.
 * Once this is done we could think of implementing multi threaded Lazy SMP search.
 */
public class SearchThreadContext {

    /**
     * Movelists used during iterative deepening and negamax. We need only at most max play instances during search.
     * which are always reused during recursive search.
     */
    private MoveList[] movelists = new MoveList[GameContext.MAX_PLY];

    @Getter
    private HistoryHeuristic historyHeuristic = new HistoryHeuristic();

    @Getter
    private CounterMoveHeuristic counterMoveHeuristic = new CounterMoveHeuristic();

    @Getter
    private KillerMoves killerMoves = new KillerMoves();

    @Getter
    private IntCache pvCache = new IntCache(16);

    public SearchThreadContext() {
        for (int i = 0; i < movelists.length; i++) {
            movelists[i] = Factory.getDefaults().moveList.create();
        }
    }

    public MoveList getCleanedMoveList(int ply) {
        MoveList moveList = movelists[ply];
        moveList.reset();
        return moveList;
    }
}
