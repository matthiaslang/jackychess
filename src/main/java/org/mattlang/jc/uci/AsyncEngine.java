package org.mattlang.jc.uci;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

import java.util.concurrent.CompletableFuture;

public class AsyncEngine {

    private CompletableFuture<Move> result;
    private Board board;
    private Color who2Move;

    public CompletableFuture<Move> start(final Board board) {
        this.board = board.copy();
        CompletableFuture<Move> future
                = CompletableFuture.supplyAsync(() -> new Engine(board.copy()).go(who2Move));

        result = future;
        return future;
    }

    public Move stop() {
        if (result != null) {
            result.cancel(true);
            try {
                result.wait(2000);
            } catch (InterruptedException | IllegalMonitorStateException e) {
                //e.printStackTrace();
            }
            result = null;
        }
        // as long as we have no iterative deepening, just retun a small search result:
        return new Engine(board,2).go(who2Move);
    }

    public void setWho2Move(Color who2Move) {
        this.who2Move = who2Move;
    }

    public Color getWho2Move() {
        return who2Move;
    }
}
