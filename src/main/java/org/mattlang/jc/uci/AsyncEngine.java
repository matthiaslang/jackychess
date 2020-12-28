package org.mattlang.jc.uci;

import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

import static org.mattlang.jc.uci.UciProcessor.OP_QUIESCENCE;
import static org.mattlang.jc.uci.UciProcessor.OP_THINKTIME;

public class AsyncEngine {

    private CompletableFuture<Move> result;

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public CompletableFuture<Move> start(final GameState gameState) {
        GoParameter goParams = new GoParameter();
        goParams.infinite = true;
        return start(gameState, goParams, new HashMap<>());
    }

    public CompletableFuture<Move> start(GameState gameState, GoParameter goParams, Map<String, Long> options) {
        SearchParameter searchParams = Factory.createDefaultParameter();
        if (options.containsKey(OP_THINKTIME)) {
            searchParams.setTimeout(options.get(OP_THINKTIME) * 1000);
        }
        if (options.containsKey(OP_QUIESCENCE)) {
            searchParams.setMaxQuiescenceDepth(options.get(OP_QUIESCENCE).intValue());
        }

        // if we have special "go" parameters, then override thinktime:
        if (!goParams.infinite) {
            long restTime;
            long restMoves;
            if (gameState.getWho2Move() == Color.WHITE) {
                restTime = goParams.wtime;
            } else {
                restTime = goParams.btime;
            }
            restMoves = goParams.movestogo;
            long averageTimeForThisMove = restTime/restMoves;
            searchParams.setTimeout(averageTimeForThisMove);

        }
        Factory.setDefaults(searchParams);
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
