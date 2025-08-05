package org.mattlang.jc.uci;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.atomic.AtomicReference;

import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.NegaMaxResult;

/**
 * Collects the best move during asynchrone engine call.
 */
public class BestMoveCollector implements IterativeDeepeningListener {

    private AtomicReference<NegaMaxResult> bestMoveSoFar;

    public BestMoveCollector(NegaMaxResult newBestRoundResult) {
        this.bestMoveSoFar = new AtomicReference<>(requireNonNull(newBestRoundResult));
    }

    @Override
    public void updateBestRoundMove(NegaMaxResult newBestRoundResult) {
        this.bestMoveSoFar.set(requireNonNull(newBestRoundResult));
    }

    public NegaMaxResult getBestMove() {
        return bestMoveSoFar.get();
    }
}
