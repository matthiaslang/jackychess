package org.mattlang.jc.engine.search;

import lombok.Getter;
import org.mattlang.jc.Factory;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.parameval.PawnCache;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.moves.MoveIterationPreparer;

import static org.mattlang.jc.Constants.MAX_PLY_INDEX;

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
    private MoveIterationPreparer[] moveIterationPreparers = new MoveIterationPreparer[MAX_PLY_INDEX];

    @Getter
    private HistoryHeuristic historyHeuristic = new HistoryHeuristic();

    @Getter
    private CounterMoveHeuristic counterMoveHeuristic = new CounterMoveHeuristic();

    @Getter
    private KillerMoves killerMoves = new KillerMoves();

    private PawnCache pawnCache = new PawnCache(13);

    @Getter
    private OrderCalculator orderCalculator;

    private EvaluateFunction evaluate;

    public SearchThreadContext() {
        resetMoveLists();
        orderCalculator = new OrderCalculator(this, getEvaluate());
    }

    public void reset() {
        resetMoveLists();
        historyHeuristic.reset();
        counterMoveHeuristic.reset();
        killerMoves.reset();
        pawnCache.reset();
        orderCalculator = new OrderCalculator(this, getEvaluate());
    }

    public MoveIterationPreparer getMoveIterationPreparer(int ply) {
        return moveIterationPreparers[ply];
    }

    public void resetMoveLists() {
        for (int i = 0; i < moveIterationPreparers.length; i++) {
            moveIterationPreparers[i] = Factory.getDefaults().moveiterationPreparer.create();
        }
    }

    /**
     * Returns an lazy initialized evaluation function which is hold for the thread during the complete game.
     *
     * @return
     */
    public EvaluateFunction getEvaluate() {
        if (evaluate == null) {
            evaluate = Factory.getDefaults().evaluateFunction.create();
            evaluate.setPawnCache(pawnCache);
        }
        return evaluate;
    }


}
