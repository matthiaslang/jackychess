package org.mattlang.jc.uci;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.atomic.AtomicReference;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;

/**
 * Collects the best move during asynchrone engine call.
 */
public class BestMoveCollector implements IterativeDeepeningListener {

    private AtomicReference<Move> bestMoveSoFar;

    public BestMoveCollector(Move bestMoveSoFar) {
        this.bestMoveSoFar = new AtomicReference<>(requireNonNull(bestMoveSoFar));
    }

    @Override
    public void updateBestRoundMove(Move newBestMove) {
        this.bestMoveSoFar.set(requireNonNull(newBestMove));
    }

    public Move getBestMove() {
        return bestMoveSoFar.get();
    }
}
