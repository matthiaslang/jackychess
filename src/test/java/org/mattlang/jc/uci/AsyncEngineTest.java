package org.mattlang.jc.uci;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;

public class AsyncEngineTest {

    @Test
    public void start() throws ExecutionException, InterruptedException, IOException {
        initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();
        ;
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 500;

        CompletableFuture<Move> future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());

        future.thenAccept(move -> System.out.println(move.toStr()));
        future.get();
    }

    @Test
    public void stopShortlyAfterStart() throws ExecutionException, InterruptedException, IOException {
        initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();
        ;
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 50;

        CompletableFuture<Move> future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());
        // in ultra short time games a stop could nearly directly after the "go" command happen.
        // the engine should properly responde with a found so far best move:
        Move move = asyncEngine.stop();
        System.out.println(move.toStr());

    }
}