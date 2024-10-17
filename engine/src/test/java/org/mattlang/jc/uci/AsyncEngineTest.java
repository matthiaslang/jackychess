package org.mattlang.jc.uci;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.util.Logging;

public class AsyncEngineTest {

    @Test
    public void start() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();
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
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();
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

    @Test
    public void stopShortlyAfterStart2() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 150;

        CompletableFuture<Move> future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());
        // in ultra short time games a stop could nearly directly after the "go" command happen.
        // the engine should properly responde with a found so far best move:
        Thread.sleep(10);

        Move move = asyncEngine.stop();
        System.out.println(move.toStr());

    }

    @Test
    public void stopShortlyAfterStartAndThenRestart()
            throws ExecutionException, InterruptedException, IOException, TimeoutException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
        board.setStartPosition();
        board.switchSiteToMove();

        AsyncEngine asyncEngine = new AsyncEngine();
        GoParameter goparams = new GoParameter();
        goparams.movetime = 50000000;

        CompletableFuture<Move> future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());
        // in ultra short time games a stop could nearly directly after the "go" command happen.
        // the engine should properly responde with a found so far best move:
        Thread.sleep(100);
        Move move = asyncEngine.stop();
        System.out.println(move.toStr());

        // now restart directly with next "go":
        board = Factory.getDefaults().boards.create();
        board.setStartPosition();
        board.switchSiteToMove();

        future = asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());
        Thread.sleep(20);
        // and stop again:
        move = asyncEngine.stop();
        System.out.println(move.toStr());

        // get will block forever if the executor job has not already started before stop has been called.
        // so this should be used with care... in the real code we do never use get() but only thenAccept
        future.get();
    }

    @Test
    public void simultaneouslyStarts() throws ExecutionException, InterruptedException, IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();
        BoardRepresentation board = Factory.getDefaults().boards.create();
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
        CompletableFuture<Move> future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());
        // start directly afterwards again
        future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());
        // and again...
        future =
                asyncEngine.start(new GameState(board), goparams, new ConfigValues(), new GameContext());

        // but we should not get any exceptions
        future.thenAccept(move -> System.out.println(move.toStr()));
        future.get();
    }
}