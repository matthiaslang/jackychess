package org.mattlang.jc.uci;

import java.util.concurrent.CompletableFuture;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

public class AsyncEngine {

    private Engine engine = new Engine();

    private CompletableFuture<Move> result;

    public CompletableFuture<Move> start(final Board board) {
        CompletableFuture<Move> future
                = CompletableFuture.supplyAsync(() -> new Engine(board.copy()).go());

        result = future;
        return future;
    }

    public void stop() {
        if (result != null) {
            result.cancel(true);
            try {
                result.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = null;
        }
    }

    public Move getBestMoveSoFar() {
        // for now simply return a small depth search instead
        return engine.stop();
    }
}
