package org.mattlang.jc.uci;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncEngine {

    private CompletableFuture<Move> result;


    public CompletableFuture<Move> start(final GameState gameState) {
        // reset Factories + Caches:
        Factory.setDefaults(Factory.createDefaultParameter());
        Factory.getDefaults().log();

        CompletableFuture<Move> future
                = CompletableFuture.supplyAsync(() -> new Engine().go(gameState));

        result = future;
        return future;
    }

    public Optional<Move> stop() {
        if (result != null) {
            result.cancel(true);
            try {
                return Optional.of(result.get(2000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException | IllegalMonitorStateException | ExecutionException | TimeoutException e) {
                //e.printStackTrace();
            }
            result = null;
        } else {
            // stop without go...?

        }
        return Optional.empty();
    }

}
