package org.mattlang.jc.engine.search;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;
import static org.mattlang.jc.util.LoggerUtils.fmtSevere;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.MoveValidator;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class IterativeDeepeningPVS implements IterativeDeepeningSearch, SearchListener {

    private static final Logger LOGGER = Logger.getLogger(IterativeDeepeningPVS.class.getSimpleName());
    public static final IterativeDeepeningListener NOOP_LISTENER = bestMove -> {

    };

    /**
     * Does not bring an improvement: the depth skip makes the performance/results worse... so we dont use it
     */
    // Laser based SMP skip
    //    private static final int[] SMP_SKIP_DEPTHS = { 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 };
    //    private static final int[] SMP_SKIP_AMOUNT = { 1, 2, 1, 2, 3, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6 };
    //    private static final int SMP_MAX_CYCLES = SMP_SKIP_AMOUNT.length;

    /**
     * worker number if this iterative deepening is running inside a worker thread.
     */
    private int workerNumber;

    /**
     * Cycle index, if run as worker thread.
     */
    private int cycleIndex;

    /**
     * true if this is running in a worker thread; false if running as main thread. The main thread
     * is responsible to deliver UCI information and delivers the result.
     */
    private boolean isWorker = false;

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    private boolean useAspirationWindow = Factory.getDefaults().getConfig().aspiration.getValue();

    private EffectiveBranchFactor ebf = new EffectiveBranchFactor();

    private MoveValidator moveValidator = new MoveValidator();

    /**
     * copy of the game state when starting search.
     */
    private GameState gameState;

    private StopWatch watch;
    private IterativeDeepeningListener listener = NOOP_LISTENER;

    public IterativeDeepeningPVS(int workerNumber) {
        this.workerNumber = workerNumber;
        //        cycleIndex = (workerNumber - 1) % SMP_MAX_CYCLES;
        isWorker = workerNumber > 0;
    }

    public IterativeDeepeningPVS(NegaMaxAlphaBetaPVS negaMaxAlphaBeta) {
        this(0);
        this.negaMaxAlphaBeta = negaMaxAlphaBeta;
    }

    public IterativeDeepeningPVS() {
    }

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(gameState, gameContext, maxDepth).getSavedMove();
    }

    @Override
    public IterativeSearchResult iterativeSearch(GameState gameState, GameContext gameContext, int maxDepth) {
        negaMaxAlphaBeta.reset();
        negaMaxAlphaBeta.resetStatistics();
        negaMaxAlphaBeta.setIsWorker(isWorker);
        this.gameState = gameState.copy();
        lastCurrMove = 0;

        SearchThreadContext stc = SearchThreadContexts.CONTEXTS.getContext(workerNumber);

        LOGGER.info("iterative search on " + gameState.getFenStr());

        watch = new StopWatch();
        watch.start();
        negaMaxAlphaBeta.setSearchListener(this);

        long stopTime = System.currentTimeMillis() + timeout;

        gameContext.initNewMoveSearch(gameState);
        ebf.clear();
        ArrayList<IterativeRoundResult> rounds = new ArrayList<>();

        int startDepth = workerNumber == 0 ? 1 : 2;
        int maxEffDepth = workerNumber > 0 ? maxDepth + 1 : maxDepth;

        IterativeRoundResult lastResults = new IterativeRoundResult(null, new StopWatch());
        try {
            int currdepth = startDepth;

            while (currdepth < maxDepth) {
                currdepth++;
                if (isWorker) {
                    currdepth = adjustDepthForWorker(currdepth);
                    if (currdepth > maxDepth) {
                        break;
                    }
                }

                stc.resetKillers();

                IterativeRoundResult irr =
                        searchRound(stc, watch, lastResults, gameState, gameContext, currdepth, stopTime);
                lastResults = irr;
                rounds.add(irr);
                if (irr.isCheckMate()) {
                    break;
                }
            }
        } catch (TimeoutException te) {

            String ebfReport = format("EBF: %s", ebf.report());
            if (!isWorker) {
                UCILogger.log(ebfReport);
            }
            IterativeSearchResult isr = new IterativeSearchResult(rounds, ebfReport);
            gameContext.addStatistics(negaMaxAlphaBeta.getStatistics());
            logIsr(isr);
            return isr;
        } catch (Throwable e) {
            throw new SearchException(gameState, gameContext, rounds, ebf.report(), e);
        }

        String ebfReport = format("EBF: %s", ebf.report());
        if (!isWorker) {
            UCILogger.log(ebfReport);
            LOGGER.info(ebfReport);
        }

        IterativeSearchResult isr = new IterativeSearchResult(rounds, ebfReport);
        logIsr(isr);
        gameContext.addStatistics(negaMaxAlphaBeta.getStatistics());
        return isr;
    }

    private void callListener(Move move) {
        if (!isWorker) {
            listener.updateBestRoundMove(move);
        }
    }

    @Override
    public void registerListener(IterativeDeepeningListener listener) {
        this.listener = requireNonNull(listener);
    }

    /**
     * adjusts the depth for workers. We dont do this for now, as it does not give any improvement,
     * indeed it makes the performance and elo worse.
     *
     * @param currDepth
     * @return
     */
    private int adjustDepthForWorker(int currDepth) {
        //        if ((currDepth + cycleIndex) % SMP_SKIP_DEPTHS[cycleIndex] == 0) {
        //            currDepth += SMP_SKIP_AMOUNT[cycleIndex];
        //        }
        return currDepth;
    }

    private void logIsr(IterativeSearchResult isr) {
        if (!isWorker) {
            String bestMove = isr.getSavedMove() != null ? isr.getSavedMove().toStr() : "none";
            String rslt = isr.getRslt() != null ? isr.getRslt().toLogString() : "no results";
            LOGGER.info("best move: " + bestMove + " " + rslt);
        }
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a1");
    private int lastCurrMove = 0;

    @Override
    public void updateCurrMove(int currMove, int currMoveScore, int targetDepth, int selDepth, long nodesVisited) {

        long duration = watch.getCurrDuration();
        long nps = duration == 0 ? nodesVisited : nodesVisited * 1000 / duration;

        UCI.instance.putCommand("info depth " + targetDepth +
                " seldepth " + selDepth +
                " score cp " + currMoveScore + " nodes " + nodesVisited
                + " nps " + nps
                + " time " + duration);
        if (currMove != 0) {
            moveWrapper.fromLongEncoded(currMove);
            UCI.instance.putCommand("info currmove " + moveWrapper.toUCIString(gameState.getBoard()));
            lastCurrMove = currMove;
        } else if (lastCurrMove != 0) {
            moveWrapper.fromLongEncoded(lastCurrMove);
            UCI.instance.putCommand("info currmove " + moveWrapper.toUCIString(gameState.getBoard()));
        }
    }

    @AllArgsConstructor
    @Getter
    static class IterativeRoundResult {

        private final NegaMaxResult rslt;
        private final StopWatch roundWatch;

        public boolean isCheckMate() {
            return Math.abs(Math.abs(rslt.directScore) - Weights.KING_WEIGHT) < 100;
        }

        public boolean hasResults() {
            return rslt != null;
        }
    }

    private IterativeRoundResult searchRound(SearchThreadContext stc, StopWatch watch,
            IterativeRoundResult lastRoundResults,
            GameState gameState, GameContext gameContext, int currdepth,
            long stopTime) {

        StopWatch roundWatch = new StopWatch();

        if (!isWorker) {
            UCI.instance.putCommand("info depth " + currdepth);
        }
        roundWatch.start();
        Window aspWindow = new Window(ALPHA_START, BETA_START);

        NegaMaxResult rslt = null;

        if (useAspirationWindow && currdepth >= 3 && lastRoundResults.hasResults()) {
            aspWindow.limitWindow(lastRoundResults.getRslt());
            rslt = searchWithAspirationWindow(stc, aspWindow, gameState, gameContext, stopTime,
                    currdepth);

        } else {
            rslt = negaMaxAlphaBeta.searchWithScore(stc, gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime);
        }

        if (rslt.savedMove != null) {
            lastCurrMove = rslt.savedMove.getMoveInt();
            printRoundInfo(gameContext, gameState, rslt, watch, negaMaxAlphaBeta);
            callListener(rslt.savedMove);
            validate(gameState, rslt);
        } else {
            // todo why does this happen that no best move gets returned from nega max search...
            // we need to further analyze this situation.
            UCILogger.log("no result from negamax for window: " + aspWindow.descr());
            LOGGER.warning("no result from negamax on iteration depth:" + currdepth);
            LOGGER.info("negamax result: " + rslt);
            negaMaxAlphaBeta.getStatistics().logStatistics();
        }

        roundWatch.stop();
        ebf.update(currdepth, roundWatch.getDuration(), negaMaxAlphaBeta.getNodesVisited());
        return new IterativeRoundResult(rslt, roundWatch);
    }

    private NegaMaxResult searchWithAspirationWindow(SearchThreadContext stc,
            Window aspWindow, GameState gameState, GameContext gameContext,
            long stopTime, int currdepth) {

        LOGGER.fine(format("aspiration start on depth %s %s", currdepth, aspWindow.descr()));

        NegaMaxResult rslt = negaMaxAlphaBeta.searchWithScore(stc, gameState, gameContext,
                currdepth,
                aspWindow.getAlpha(), aspWindow.getBeta(),
                stopTime);

        while (aspWindow.outsideWindow(rslt)) {
            aspWindow.widenWindow(rslt);
            LOGGER.fine(format("aspiration widened to %s", aspWindow.descr()));
            rslt = negaMaxAlphaBeta.searchWithScore(stc, gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime);
        }
        LOGGER.fine(format("aspiration stabilized: %s", aspWindow.descr()));
        return rslt;
    }

    private void printRoundInfo(
            GameContext gameContext,
            GameState gameState,
            NegaMaxResult rslt,
            StopWatch watch,
            AlphaBetaSearchMethod negaMaxAlphaBeta) {
        if (!isWorker) {
            long nodes = negaMaxAlphaBeta.getNodesVisited();
            long duration = watch.getCurrDuration();
            long nps = duration == 0 ? nodes : nodes * 1000 / duration;

            long hashfull = gameContext.ttCache.calcHashFull();
            //        long hashfull = gameContext.ttc.getUsagePercentage();
            UCI.instance.putCommand("info depth " + rslt.targetDepth +
                    " seldepth " + rslt.selDepth +
                    " score cp " + rslt.max + " nodes " + nodes
                    + " hashfull " + hashfull
                    + " nps " + nps
                    + " time " + duration
                    + " pv " + rslt.pvList.toPvStr(gameState.getBoard()));
            UCI.instance.putCommand("info currmove " + rslt.savedMove.toUCIString(gameState.getBoard()));
        }
    }

    public void validate(GameState gameState, NegaMaxResult rslt) {
        if (LOGGER.isLoggable(Level.WARNING)) {
            BoardRepresentation board = gameState.getBoard().copy();

            Color who2Move = gameState.getWho2Move();
            for (Move move : rslt.pvList.getPvMoves()) {

                boolean legal = moveValidator.isLegalMove(board, move, who2Move);

                if (legal) {
                    board.domove(move);
                } else {
                    LOGGER.warning(
                            "depth: " + rslt.targetDepth + " Illegal PV Move " + move.toUCIString(board) + " in "
                                    + rslt.toLogString());
                    break;

                }
                who2Move = who2Move.invert();
            }
        }

        // test the best move itself:
        if (LOGGER.isLoggable(SEVERE)) {
            BoardRepresentation board = gameState.getBoard().copy();
            boolean legal = moveValidator.isLegalMove(board, rslt.savedMove, gameState.getWho2Move());
            if (!legal) {
                LOGGER.log(SEVERE, fmtSevere(gameState, "Illegal Best Move " + rslt.savedMove.toUCIString(board)));
            }
        }
    }
}
