package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.MAX_PLY_INDEX;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.EvaluateFunctionFactory;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.moves.StagedMoveIterationPreparer;

import lombok.Getter;

/**
 * Holds variables used during a search for one thread which is used for all nested iterative deepening and negamax
 * search.
 * This includes movelist and all heuristical data like killer heuristic, history heuristic, pv cache, etc.
 * <p>
 * All structures which are needed by one search thread should be refactored in this class finally.
 */
public class SearchThreadContext {

    /**
     * Movelists used during iterative deepening and negamax. We need only at most max play instances during search.
     * which are always reused during recursive search.
     */
    private StagedMoveIterationPreparer[] moveIterationPreparers = new StagedMoveIterationPreparer[MAX_PLY_INDEX];

    @Getter
    private HistoryHeuristic historyHeuristic = new HistoryHeuristic();

    @Getter
    private CounterMoveHeuristic counterMoveHeuristic = new CounterMoveHeuristic();

    @Getter
    private KillerMoves killerMoves = new KillerMoves();

    @Getter
    private SearchThreadContextCache cache = new SearchThreadContextCache();

    @Getter
    private OrderCalculator orderCalculator;

    private EvaluateFunction evaluate;

    public SearchThreadContext() {
        initMoveLists();
        orderCalculator = new OrderCalculator(this);
    }

    /**
     * Resets all state holding data structures like caches, history caches,  etc.
     */
    public void reset() {
        historyHeuristic.reset();
        counterMoveHeuristic.reset();
        killerMoves.reset();
        cache.reset();
        orderCalculator = new OrderCalculator(this);
    }

    public StagedMoveIterationPreparer getMoveIterationPreparer(int ply) {
        return moveIterationPreparers[ply];
    }

    public void initMoveLists() {
        for (int i = 0; i < moveIterationPreparers.length; i++) {
            moveIterationPreparers[i] = new StagedMoveIterationPreparer();
        }
    }

    /**
     * Returns an lazy initialized evaluation function which is hold for the thread during the complete game.
     *
     * @return
     */
    public EvaluateFunction getEvaluate() {
        if (evaluate == null) {
            evaluate = EvaluateFunctionFactory.createConfiguredEvaluateFunction();
            evaluate.associateThreadCache(cache);
        }
        return evaluate;
    }

    public void resetKillers() {
        killerMoves.reset();
    }
}
