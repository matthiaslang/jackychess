package org.mattlang.jc.uci;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;

import junit.framework.TestCase;

public class AsyncEngineTest  {

    @Test
    public void start() throws ExecutionException, InterruptedException {
        Board board = new Board();
        board.setStartPosition();;

        AsyncEngine asyncEngine = new AsyncEngine();
        CompletableFuture<Move> future = asyncEngine.start(board);

        future.thenAccept(move -> System.out.println(move.toStr()));
        future.get();
    }
}