package org.mattlang.jc.uci;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncEngine {

    private CompletableFuture<Move> result;
    private Board board;
    private Color who2Move;

    public CompletableFuture<Move> start(final Board board) {
        // reset Factories + Caches:
        Factory.setDefaults(Factory.createDefaultParameter());

        this.board = board.copy();
        CompletableFuture<Move> future
                = CompletableFuture.supplyAsync(() -> new Engine(board.copy()).go(who2Move));

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

    public void setWho2Move(Color who2Move) {
        this.who2Move = who2Move;
    }

    public Color getWho2Move() {
        return who2Move;
    }
}
