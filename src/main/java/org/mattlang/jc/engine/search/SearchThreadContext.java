package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.uci.GameContext;

/**
 * Holds variables used during a search for one thread which is used for all nested iterative deepening and negamax
 * search.
 * Mainly it is reused movelists.
 */
public class SearchThreadContext {

    private MoveList[] movelists = new MoveList[GameContext.MAX_PLY];

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
