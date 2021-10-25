package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.engine.tt.TTEntry;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;

/**
 * Negamax with Alpha Beta Pruning. Supports PVS Search which could be optional activated.
 * Supports TT Cache
 */
public class NegaMaxAlphaBetaPVS implements AlphaBetaSearchMethod, StatisticsCollector {

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;

    /** number of searched moves to start LMR if activated.*/
    private static final int LMR_AFTER_N_SEARCHED_MOVES = 3;
    /** number of additional searched moves to do a higher reduction for LMR. */
    private static final int LMR_N_MOVES_REDUCE_MORE = 6;

    private EvaluateFunction evaluate;

    private int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    private boolean useHistoryHeuristic = Factory.getDefaults().getConfig().useHistoryHeuristic.getValue();
    private boolean useKillerMoves = Factory.getDefaults().getConfig().useKillerMoves.getValue();

    private boolean doChessExtension = Factory.getDefaults().getConfig().chessExtension.getValue();

    private PVTriangularArray pvArray = new PVTriangularArray();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int quiescenceNodesVisited = 0;

    private int nodes;

    private int extensionCounter=0;

    private OrderCalculator orderCalculator;

    private int cutOff;

    private boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();

    private boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();
    private boolean useLateMoveReductions = Factory.getDefaults().getConfig().useLateMoveReductions.getValue();

    private HistoryHeuristic historyHeuristic = null;

    private KillerMoves killerMoves = null;

    private ExtensionsInfo extensionsInfo = new ExtensionsInfo();

    private SearchContext searchContext;

    public NegaMaxAlphaBetaPVS() {
        reset();
    }

    public NegaMaxAlphaBetaPVS(EvaluateFunction evaluate) {
        reset();
        this.evaluate = evaluate;
    }

    @Override
    public Move search(GameState gameState, GameContext context, int depth) {
        assert depth > 0;
        reset();

        searchWithScore(gameState, context, depth,
                        ALPHA_START, BETA_START,
                        stopTime, OrderHints.NO_HINTS);
        return new MoveImpl(searchContext.getSavedMove());
    }

    private void initContext(GameContext context) {
          killerMoves = context.getKillerMoves();
          historyHeuristic = context.getHistoryHeuristic();
    }

    public void resetStatistics() {
        nodesVisited = 0;
        quiescenceNodesVisited = 0;
        nodes = 0;
        cutOff = 0;
        extensionsInfo = new ExtensionsInfo();
    }

    public void reset() {
        resetStatistics();
        evaluate = Factory.getDefaults().evaluateFunction.create();
    }

    private int negaMaximize(int ply, int depth, Color color,
            int alpha, int beta) {
        nodesVisited++;

        if (searchContext.isRepetition()) {
            return Weights.REPETITION_WEIGHT;
        }

        TTEntry tte = searchContext.getTTEntry(color);
        if (tte != null && tte.getDepth() >= depth && ply != 1) {
            if (tte.isExact()) // stored value is exact
                return adjustScore(tte.getValue(), ply);
            if (tte.isLowerBound() && tte.getValue() > alpha)
                alpha = adjustScore(tte.getValue(), ply); // update lowerbound alpha if needed
            else if (tte.isUpperBound() && tte.getValue() < beta)
                beta = adjustScore(tte.getValue(), ply); // update upperbound beta if needed
            if (alpha >= beta)
                return adjustScore(tte.getValue(), ply); // if lowerbound surpasses upperbound
        }

        if (depth == 0) {
            return quiesce(ply+1, -1, color, alpha, beta);
        }

        boolean areWeInCheck = searchContext.isInCheck(color);

        checkTimeout();

        /**
         * null move reduction:
         * only, for non pv nodes,
         * and no null move has already been chosen in this row
         * and we are not in end game (because of zugzwang issues)
         * and we are not in check (also for zugzwang)
         */
        if (useNullMoves &&
                depth > 2 &&
                beta - alpha <= 1 &&
                searchContext.getNullMoveCounter() == 0 &&
                !areWeInCheck &&
                searchContext.isOpeningOrMiddleGame()
        ) {
            int R = (depth > 6) ? 3 : 2;

            searchContext.doPrepareNullMove();
            int eval = -negaMaximize(ply + 1, depth - R, color.invert(), -beta, -beta + 1);
            searchContext.undoNullMove();

            if (eval >= beta) {
                return eval;
            }
        }

        int max = alpha;

        int bestMove = 0;

        try (MoveList moves = searchContext.generateMoves(color)) {
            if (moves.isCheckMate()) {
                return determineCheckMateOrPatt(ply, areWeInCheck);
            }

            sortMoves(ply, depth, color, moves);

            nodes += moves.size();

            boolean firstChild = true;

            depth = checkToExtend(areWeInCheck, color, depth);

            int searchedMoves = 0;
            for (MoveCursor moveCursor : moves) {
                searchContext.doMove(moveCursor);

                searchedMoves++;

                int score;
                if (firstChild) {
                    /**
                     * do full search (for pvs search on the first move, or if pvs search is deactivated)
                     */
                    score = -negaMaximize(ply + 1, depth - 1, color.invert(), -beta, -max);
                    if (doPVSSearch) {
                        firstChild = false;
                    }
                } else {
                    /**
                     * in pvs search try to do a 0 window search on all other than the first move:
                     * we combine this with late move reductions if possible
                     */
                    int R = 0;
                    /**
                     * combine it with late move reductions if possible and activated
                     */
                    boolean doLMR = canWeDoLateMoveReduction(searchedMoves, depth, moveCursor, areWeInCheck);
                    if (doLMR) {
                        R = 1;
                        if (searchedMoves > LMR_AFTER_N_SEARCHED_MOVES + LMR_N_MOVES_REDUCE_MORE && depth > 4) {
                            R = depth / 3;
                        }
                    }
                    // pvs try 0 window with or without late move reduction
                    score = -negaMaximize(ply + 1, depth - 1 - R, color.invert(), -max - 1, -max);

                    /**
                     * do a full window search in pvs search if score is out of our max, beta window:
                     */
                    if (max < score && score < beta) {
                        score = -negaMaximize(ply + 1, depth - 1 - R, color.invert(), -beta, -max);
                    }
                }

                /** save score for all root moves: */
                searchContext.updateRootMoveScore(depth, moveCursor.getMoveInt(), score);

                searchContext.undoMove(moveCursor);
                if (score > max) {
                    max = score;

                    bestMove = moveCursor.getMoveInt();

                    pvArray.set(bestMove, ply);
                    searchContext.updateRootBestMove(depth, bestMove, score);

                    if (max >= beta) {
//                        if (!areWeInCheck) {
                            updateCutOffHeuristics(ply, depth, color, bestMove, moveCursor);
//                        }
                        cutOff++;
                        break;
                    }

                }

                // update "bad" heuristic
                updateBadHeuristic(depth, color, moveCursor);
            }
        }

        // save score and best move info in tt:
        searchContext.storeTT(color, max, max, beta, depth, bestMove);

        return max;
    }

    private void updateBadHeuristic(int depth, Color color, MoveCursor moveCursor) {
        if (useHistoryHeuristic && !moveCursor.isCapture()) {
            historyHeuristic.updateBad(color, moveCursor, depth);
        }
    }

    private void updateCutOffHeuristics(int ply, int depth, Color color, int bestMove, MoveCursor moveCursor) {
        if (!moveCursor.isCapture()) {
            if (useHistoryHeuristic) {
                historyHeuristic.update(color, moveCursor, depth);
            }
            if (useKillerMoves) {
                killerMoves.addKiller(color, bestMove, ply);
            }
        }
    }

    /**
     * No more legal moves means either we are check mate or we have a stale mate.
     *
     * @param ply
     * @param areWeInCheck
     * @return
     */
    private int determineCheckMateOrPatt(int ply, boolean areWeInCheck) {
        if (areWeInCheck) {
            // no more legal moves, that means we have checkmate:
            // in negamax "our" score is highest, so we need to return the negative king weight adjusted by ply:
            return -KING_WEIGHT + ply;
        } else {
            return -PATT_WEIGHT;
        }
    }

    /**
     * Determines if we should try a late move reduction.
     * We do it for later moves (after the 3 best moves in the ordered
     *
     * @param searchedMoves
     * @param depth
     * @param areWeInCheck
     * @return
     */
    private boolean canWeDoLateMoveReduction(int searchedMoves, int depth, MoveCursor moveCursor,
            boolean areWeInCheck) {
        return useLateMoveReductions &&
                searchedMoves > LMR_AFTER_N_SEARCHED_MOVES &&
                depth > 3 &&
                !moveCursor.isCapture() &&
                moveCursor.getFigureType() != FigureType.Pawn.figureCode &&
                moveCursor.getOrder() >= OrderCalculator.LATE_MOVE_REDUCTION_BORDER &&
                !areWeInCheck;
    }

    /**
     * Adjust Mate scores by the ply.
     *
     * We do this by adjusting it with the current given ply.
     *
     * @param score
     * @param ply
     * @return
     */
    private int adjustScore(int score, int ply) {
        int sc = score;
        if (sc > KING_WEIGHT - 1000) {
            sc = KING_WEIGHT - ply;
        } else if (sc < -(KING_WEIGHT - 1000)) {
            sc = -KING_WEIGHT + ply;
        }
        return sc;
    }


    private void checkTimeout() {
        if (stopTime != 0 && System.currentTimeMillis() > stopTime) {
            throw new TimeoutException();
        }
        if (nodesVisited % 10000 == 0) {
            if (Thread.interrupted()) {
                throw new TimeoutException();
            }
        }
    }

    private int quiesce(int ply, int depth, Color color, int alpha, int beta) {
        nodesVisited++;

        if (searchContext.isRepetition()) {
            return Weights.REPETITION_WEIGHT;
        }

        int eval = searchContext.eval(color);

        /* are we too deep? */
        if (depth < -maxQuiescenceDepth) {
            searchContext.storeTT(color, eval, alpha, beta, depth, 0);
            return eval;
        }

        checkTimeout();

        /* check with the evaluation function */
        int x = eval;
        if (x >= beta)
            return beta;
        if (x > alpha)
            alpha = x;

        boolean areWeInCheck = searchContext.isInCheck(color);

        try (MoveList moves = searchContext.generateNonQuietMoves(color)) {
            if (moves.isCheckMate()) {
                return determineCheckMateOrPatt(ply, areWeInCheck);
            }

            nodes += moves.size();
            quiescenceNodesVisited++;
            searchContext.adjustSelDepth(depth);

            // sort just by MMV-LVA, as we have no pv infos currently in quiescence...
            sortMoves(ply, depth, color, moves);

            /* loop through the capture moves */
            for (MoveCursor moveCursor : moves) {
                searchContext.doMove(moveCursor);
                x = -quiesce(ply + 1, depth - 1, color.invert(), -beta, -alpha);
                searchContext.undoMove(moveCursor);
                if (x > alpha) {
                    if (x >= beta)
                        return beta;
                    alpha = x;
                }
            }
        }

        searchContext.storeTT(color, x, alpha, beta, depth, 0);

        return alpha;
    }

    /**
     * Sorts the move list by calculating the move order first.
     *
     * @param depth
     * @param color
     * @param moves
     */
    private void sortMoves(int ply, int depth, Color color, MoveList moves) {
        int hashMove = searchContext.probeTTHashMove(color, depth);
        orderCalculator.prepareOrder(color, hashMove, ply, depth);
        moves.sort(orderCalculator);
    }

    private int checkToExtend(boolean areWeInCheck, Color color, int currDepth) {
        if (doChessExtension) {
            if (areWeInCheck) {
                return currDepth + 1;
            }
        }
        return currDepth;
    }

    @Override
    public NegaMaxResult searchWithScore(GameState gameState,
            GameContext context,
            int depth,
            int alpha, int beta, long stopTime, OrderHints orderHints) {

        searchContext = new SearchContext(gameState, context, evaluate, depth, alpha);

        this.orderCalculator = new OrderCalculator(orderHints, depth);
        this.stopTime = stopTime;

        extensionCounter = 0;

        pvArray.reset();

        initContext(context);

        int directScore = negaMaximize(1, depth, gameState.getWho2Move(), alpha, beta);

        return new NegaMaxResult(directScore, pvArray.getPvMoves(),
                searchContext);

    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    public int getNodes() {
        return nodes;
    }

    @Override
    public void collectStatistics(Map stats) {
        if (nodes > 0) {
            Map rslts = new LinkedHashMap();
            stats.put("negamax alpha/beta", rslts);
            if (nodes != 0) {
                rslts.put("visited/all Nodes", nodesVisited * 100 / nodes + "%");
            }
            rslts.put("nodesVisited", nodesVisited);
            rslts.put("nodes", nodes);
            if (maxQuiescenceDepth > 0) {
                rslts.put("quiescenceNodesVisited", quiescenceNodesVisited);
            }
            rslts.put("cutoff", cutOff);

            Map searchstatsMap = new LinkedHashMap();
            searchContext.collectStatistics(searchstatsMap);
            rslts.put("search", searchstatsMap);
        }
    }

    public NegaMaxAlphaBetaPVS setDoPVSSearch(boolean doPVSSearch) {
        this.doPVSSearch = doPVSSearch;
        return this;
    }

    public NegaMaxAlphaBetaPVS setDoCaching(boolean doCaching) {
        // todo remove...
        return this;
    }

    @Override
    public void resetCaches() {
        //ttCache = new TTCache();
    }
}
