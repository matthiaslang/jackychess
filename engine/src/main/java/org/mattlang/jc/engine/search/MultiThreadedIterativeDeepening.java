package org.mattlang.jc.engine.search;


import org.mattlang.jc.*;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.uci.GameContext;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static org.mattlang.jc.engine.evaluation.Weights.VALUE_TB_LOSS_IN_MAX_PLY;
import static org.mattlang.jc.engine.evaluation.Weights.VALUE_TB_WIN_IN_MAX_PLY;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;

public class MultiThreadedIterativeDeepening implements IterativeDeepeningSearch, StatisticsCollector, IterativeDeepeningListener {

    private static final Logger LOGGER = Logger.getLogger(MultiThreadedIterativeDeepening.class.getSimpleName());

    private final int maxThreads = ConfigValues.getConfigValues().maxThreads.getValue();

    private AtomicReference<IterativeRoundResult>[] lastIRRs = null;

    private IterativeDeepeningListener listener = IterativeDeepeningPVS.NOOP_LISTENER;

    @UciConfigParam
    private int minScoreVotingOffset = 1;

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(new SearchParameter(SearchParameter.DEFAULT_SEARCHTIME, maxDepth), gameState,
                gameContext).getSavedMove();
    }

    @Override
    public IterativeSearchResult iterativeSearch(SearchParameter searchParams, GameState gameState,
                                                 GameContext gameContext) {
        IterativeDeepeningPVS id = new IterativeDeepeningPVS(0);
        if (maxThreads == 1) {
            id.registerListener(listener);
            return id.iterativeSearch(searchParams, gameState, gameContext);
        }

        lastIRRs = new AtomicReference[maxThreads];
        for (int i = 0; i < maxThreads; i++) {
            lastIRRs[i] = new AtomicReference<>();
        }
        // start max-1 workerthreads
        List<Future<IterativeSearchResult>> futures = new ArrayList<>();
        for (int i = 1; i < maxThreads; i++) {
            futures.add(startWorker(i, searchParams, gameState, gameContext));
        }
        // for multi threading, register our listener which collects all thread results
        id.registerListener(this);

        try {
            // and afterward start the "main" within this thread as worker 0:
            IterativeSearchResult resultOfFirstThread = id.iterativeSearch(searchParams, gameState, gameContext);
            /*
                create result of the collected results of all search threads:
                we use the ebf report of the first thread, not perfect but this is anyway only used in tests/analysis.
             */

            synchronized (this) {
                IterativeRoundResult votedIRR = voteIrr();
                return new IterativeSearchResult(List.of(votedIRR), resultOfFirstThread.getEbfReport());
            }
        } finally {
            stopAllWorker(futures);
        }
    }

    private int calcVote(IterativeRoundResult irr, int minScore) {

        return (irr.rslt().max - minScore + minScoreVotingOffset) * irr.rslt().targetDepth;
    }

    private IterativeRoundResult voteIrr() {
        List<IterativeRoundResult> allResults = Arrays.stream(lastIRRs)
                .map(AtomicReference::get)
                .filter(Objects::nonNull)
                .filter(irr -> irr.rslt().savedMove != null)
                .toList();

        Integer minScore = allResults.stream()
                .map(irr -> irr.rslt().max)
                .min(Comparator.comparing(Integer::intValue))
                .orElseThrow(NoSuchElementException::new);

        HashMap<Integer, Integer> votes = new HashMap<>();
        for (IterativeRoundResult allResult : allResults) {
            votes.compute(allResult.rslt().savedMove.getMoveInt(), (k, v) -> v == null ? calcVote(allResult, minScore) : v + calcVote(allResult, minScore));
        }

        IterativeRoundResult best = allResults.getFirst();

        for (IterativeRoundResult th : allResults) {

            int bestThreadScore = best.rslt().max;
            int newThreadScore = th.rslt().max;

            List<Move> bestThreadPV = best.rslt().getPvMoves();
            List<Move> newThreadPV = th.rslt().getPvMoves();

            int bestThreadMoveVote = votes.get(best.rslt().savedMove.getMoveInt());
            int newThreadMoveVote = votes.get(th.rslt().savedMove.getMoveInt());

            boolean bestThreadInProvenWin = isWin(bestThreadScore);
            boolean newThreadInProvenWin = isWin(newThreadScore);

            boolean bestThreadInProvenLoss =
                    bestThreadScore != ALPHA_START && isLoss(bestThreadScore);
            boolean newThreadInProvenLoss =
                    newThreadScore != ALPHA_START && isLoss(newThreadScore);

            // We make sure not to pick a thread with truncated principal variation
            boolean betterVotingValue =
                    calcVote(th, minScore) * (newThreadPV.size() > 2 ? 1 : 0)
                            > calcVote(best, minScore) * (bestThreadPV.size() > 2 ? 1 : 0);

            if (bestThreadInProvenWin) {
                // Make sure we pick the shortest mate / TB conversion
                if (newThreadScore > bestThreadScore)
                    best = th;
            } else if (bestThreadInProvenLoss) {
                // Make sure we pick the shortest mated / TB conversion
                if (newThreadInProvenLoss && newThreadScore < bestThreadScore)
                    best = th;
            } else if (newThreadInProvenWin || newThreadInProvenLoss
                    || (!isLoss(newThreadScore)
                    && (newThreadMoveVote > bestThreadMoveVote
                    || (newThreadMoveVote == bestThreadMoveVote && betterVotingValue))))
                best = th;
        }

        return best;
    }

    private static final boolean isWin(int value) {
        return value >= VALUE_TB_WIN_IN_MAX_PLY;
    }

    private static final boolean isLoss(int value) {
        return value <= VALUE_TB_LOSS_IN_MAX_PLY;
    }

    @Override
    public void registerListener(IterativeDeepeningListener listener) {
        this.listener = requireNonNull(listener);
    }

    private void stopAllWorker(List<Future<IterativeSearchResult>> futures) {
        for (Future<IterativeSearchResult> future : futures) {
            future.cancel(true);
        }
    }

    private Future<IterativeSearchResult> startWorker(int workerNumber, SearchParameter searchParams,
                                                      GameState gameState, GameContext gameContext) {
        GameState copiedGame = gameState.copy();
        IterativeDeepeningPVS worker = new IterativeDeepeningPVS(workerNumber);
        worker.registerListener(this);
        return JCExecutors.EXECUTOR_SERVICE.submit(() -> {
            try {
                return worker.iterativeSearch(searchParams, copiedGame, gameContext);
            } catch (StopException stopException) {
                // do not log stop exceptions as this is the normal case. just rethrow it:
                throw stopException;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error in worker thread!", e);
                throw e;
            }
        });
    }

    @Override
    public void collectStatistics(Map stats) {

    }

    @Override
    public void resetStatistics() {

    }

    @Override
    public synchronized void updateBestRoundMove(NegaMaxResult bestRoundResult) {
        listener.updateBestRoundMove(bestRoundResult);
    }


    @Override
    public synchronized void updateIIR(IterativeRoundResult irr) {
        lastIRRs[irr.workerNumber()].set(irr);
    }
}
