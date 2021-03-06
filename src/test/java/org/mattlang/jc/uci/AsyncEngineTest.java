package org.mattlang.jc.uci;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;

public class AsyncEngineTest  {

    @Test
    public void start() throws ExecutionException, InterruptedException {
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();;
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        CompletableFuture<Move> future = asyncEngine.start(new GameState(board), new GameContext());

        future.thenAccept(move -> System.out.println(move.toStr()));
        future.get();
    }
}