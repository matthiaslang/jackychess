package org.mattlang.jc.uci;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.util.Logging;

public class AsyncEngineTest {

    @Test
    public void start() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Configurator.createBoard();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 500;

        CompletableFuture<NegaMaxResult> future =
                asyncEngine.start(new GameState(board), goparams, new GameContext());

        future.thenAccept(n -> System.out.println(n.savedMove.toStr()));
        future.get();
    }

    @Test
    public void stopShortlyAfterStart() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Configurator.createBoard();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 50;

        CompletableFuture<NegaMaxResult> future =
                asyncEngine.start(new GameState(board), goparams, new GameContext());
        // in ultra short time games a stop could nearly directly after the "go" command happen.
        // the engine should properly responde with a found so far best move:
        NegaMaxResult negaMaxResult = asyncEngine.stop();
        System.out.println(negaMaxResult.savedMove.toStr());

    }

    @Test
    public void stopShortlyAfterStart2() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Configurator.createBoard();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 150;

        CompletableFuture<NegaMaxResult> future =
                asyncEngine.start(new GameState(board), goparams, new GameContext());
        // in ultra short time games a stop could nearly directly after the "go" command happen.
        // the engine should properly responde with a found so far best move:
        Thread.sleep(10);

        NegaMaxResult negaMaxResult = asyncEngine.stop();
        System.out.println(negaMaxResult.savedMove.toStr());

    }

    @Test
    public void stopShortlyAfterStartAndThenRestart()
            throws ExecutionException, InterruptedException, IOException, TimeoutException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Configurator.createBoard();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 50000000;

        asyncEngine.start(new GameState(board), goparams, new GameContext());
        // in ultra short time games a stop could nearly directly after the "go" command happen.
        // the engine should properly respond with a found so far best move:
        Thread.sleep(100);
        NegaMaxResult negaMaxResult = asyncEngine.stop();
        System.out.println(negaMaxResult.savedMove.toStr());

        // now restart directly with next "go":
        board = Configurator.createBoard();
        board.setStartPosition();
        board.switchSiteToMove();

        CompletableFuture<NegaMaxResult> future = asyncEngine.start(new GameState(board), goparams, new GameContext());
        Thread.sleep(20);
        // and stop again:
        negaMaxResult = asyncEngine.stop();
        System.out.println(negaMaxResult.savedMove.toStr());

        // get will block forever if the executor job has not already started before stop has been called.
        // so this should be used with care... in the real code we do never use get() but only thenAccept
        // here we will get an exception since the search thread is stopped:
        ExecutionException exception = Assertions.assertThrows(ExecutionException.class, () -> {
            future.get();
        });
    }

    @Test
    public void simultaneouslyStarts() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Configurator.createBoard();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 500;

        // normaly a start should only be executed after the last start has properly finished.
        // if "start" and "stop" and then "start" again are very shortly executed, it could happen
        // that the last "start" is still running. Our code should take care to not overlap those
        // executes but block a "start" till the last "start" is really finished by a semaphore.
        // otherwise we would get Exceptions when both access the same data structures.
        CompletableFuture<NegaMaxResult> future =
                asyncEngine.start(new GameState(board), goparams, new GameContext());
        // start directly afterwards again
        future =
                asyncEngine.start(new GameState(board), goparams, new GameContext());
        // and again...
        future =
                asyncEngine.start(new GameState(board), goparams, new GameContext());

        // but we should not get any exceptions
        future.thenAccept(negaMaxResult -> System.out.println(negaMaxResult.savedMove.toStr()));
        future.get();
    }
}