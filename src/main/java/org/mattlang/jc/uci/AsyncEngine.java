package org.mattlang.jc.uci;

import java.util.concurrent.CompletableFuture;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

public class AsyncEngine {

    private CompletableFuture<Move> result;
    private Board board;

    public CompletableFuture<Move> start(final Board board) {
        this.board = board.copy();
        CompletableFuture<Move> future
                = CompletableFuture.supplyAsync(() -> new Engine(board.copy()).go());

        result = future;
        return future;
    }

    public Move stop() {
        if (result != null) {
            result.cancel(true);
            try {
                result.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = null;
        }
        // as long as we have no iterative deepening, just retun a small search result:
        return new Engine(board,2).go();
    }

}
