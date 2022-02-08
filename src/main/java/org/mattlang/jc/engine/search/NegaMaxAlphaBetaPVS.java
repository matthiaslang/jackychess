package org.mattlang.jc.engine.search;

import static java.lang.Math.abs;
import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;
import static org.mattlang.jc.uci.GameContext.MAX_PLY;
import static org.mattlang.jc.util.MoveValidator.enrichPVList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.see.SEE;
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

    public static final int VALUE_TB_WIN_IN_MAX_PLY = KING_WEIGHT - 2 * MAX_PLY;
    public static final int VALUE_TB_LOSS_IN_MAX_PLY = -VALUE_TB_WIN_IN_MAX_PLY;

    private static final SEE see = new SEE();

    private static final int[] FUTILITY_MARGIN = { 0, 200, 300, 500 };

    /**
     * number of searched moves to start LMR if activated.
     */
    private static final int LMR_AFTER_N_SEARCHED_MOVES = 3;
    /**
     * number of additional searched moves to do a higher reduction for LMR.
     */
    private static final int LMR_N_MOVES_REDUCE_MORE = 6;

    private static final int GOOD_CAPT_LOWER = OrderCalculator.GOOD_CAPTURES_SCORE - 1000000;
    private static final int GOOD_CAPT_UPPER = OrderCalculator.GOOD_CAPTURES_SCORE + 1000000;

    private EvaluateFunction evaluate;

    private int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    private boolean useHistoryHeuristic = Factory.getDefaults().getConfig().useHistoryHeuristic.getValue();
    private boolean useKillerMoves = Factory.getDefaults().getConfig().useKillerMoves.getValue();

    private boolean doChessExtension = Factory.getDefaults().getConfig().chessExtension.getValue();
    private boolean expandPv = Factory.getDefaults().getConfig().expandPv.getValue();
    private boolean mateDistancePruning = Factory.getDefaults().getConfig().mateDistancePruning.getValue();

    private PVTriangularArray pvArray = new PVTriangularArray();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int quiescenceNodesVisited = 0;

    private int extensionCounter = 0;

    private OrderCalculator orderCalculator;

    private int cutOff;

    private boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();

    private boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();
    private boolean staticNullMove = Factory.getDefaults().getConfig().staticNullMove.getValue();
    private boolean razoring = Factory.getDefaults().getConfig().razoring.getValue();
    private boolean deltaCutOff = Factory.getDefaults().getConfig().deltaCutoff.getValue();
    private boolean useLateMoveReductions = Factory.getDefaults().getConfig().useLateMoveReductions.getValue();
    private boolean futilityPruning = Factory.getDefaults().getConfig().futilityPruning.getValue();

    private HistoryHeuristic historyHeuristic = null;

    private KillerMoves killerMoves = null;

    private ExtensionsInfo extensionsInfo = new ExtensionsInfo();

    private SearchContext searchContext;

    private boolean debug=true;

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

        searchWithScore(new SearchThreadContext(), gameState, context, depth,
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
        cutOff = 0;
        extensionsInfo = new ExtensionsInfo();
    }

    public void reset() {
        resetStatistics();
        evaluate = Factory.getDefaults().evaluateFunction.create();
    }

    private int negaMaximize(int ply, int depth, Color color,
            int alpha, int beta) {
        int mateValue = KING_WEIGHT - ply;

        nodesVisited++;
        boolean not_pv = abs(beta - alpha) <= 1;

        /**************************************************************************
         * MATE DISTANCE PRUNING, a minor improvement that helps to shave off some *
         * some nodes when the checkmate is near. Basically it prevents looking    *
         * for checkmates taking longer than one we have already found. No Elo     *
         * gain expected, but it's a nice feature. Don't use it at the root,       *
         * since  this code  doesn't return a move, only a value.                  *
         **************************************************************************/
        if (mateDistancePruning && ply != 1) {
            if (alpha < -mateValue)
                alpha = -mateValue;
            if (beta > mateValue - 1)
                beta = mateValue - 1;
            if (alpha >= beta)
                return alpha;
        }

        if (searchContext.isRepetition()) {
            return Weights.REPETITION_WEIGHT;
        }

        boolean areWeInCheck = searchContext.isInCheck(color);
        depth = checkToExtend(areWeInCheck, color, depth);

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
            return quiesce(ply + 1, -1, color, alpha, beta);
        }
        checkTimeout();

        boolean applyFutilityPruning = false;

        if (not_pv && !areWeInCheck) {

            int staticEval = searchContext.eval(color);

            // use tt value as eval if possible
            if (canRefineEval(tte, staticEval)) {
                staticEval = tte.getValue();
            }

            /**************************************************************************
             * EVAL PRUNING / STATIC NULL MOVE                                         *
             **************************************************************************/

            if (staticNullMove
                    && depth < 3
                    && abs(beta - 1) > ALPHA_START + 100) {

                int eval_margin = 120 * depth;
                if (staticEval - eval_margin >= beta)
                    return staticEval - eval_margin;
            }

            /**
             * null move reduction:
             * only, for non pv nodes,
             * and no null move has already been chosen in this row
             * and we are not in end game (because of zugzwang issues)
             * and we are not in check (also for zugzwang)
             */
            if (useNullMoves &&
                    depth > 2 &&
                    searchContext.getNullMoveCounter() == 0 &&
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

            /**************************************************************************
             *  RAZORING - if a node is close to the leaf and its static score is low, *
             *  we drop directly to the quiescence search.                             *
             **************************************************************************/

            if (razoring
                    && tte == null
                    && searchContext.getNullMoveCounter() == 0
                    //	&&  !(bbPc(p, p->side, P) & bbRelRank[p->side][RANK_7]) // no pawns to promote in one move
                    && depth <= 3) {
                int threshold = alpha - 300 - (depth - 1) * 60;
                if (staticEval < threshold) {
                    int val = quiesce(ply + 1, -1, color, alpha, beta);
                    if (val < threshold)
                        return alpha;
                }
            }

            /**************************************************************************
             *  Decide  if FUTILITY PRUNING  is  applicable. If we are not in check,   *
             *  not searching for a checkmate and eval is below (alpha - margin), it   *
             *  might  mean that searching non-tactical moves at low depths is futile  *
             *  so we set a flag allowing this pruning.                                *
             **************************************************************************/

            if (futilityPruning &&
                    //                ply > 3 &&
                    depth <= 3
                    && abs(alpha) < 9000
                    && staticEval + FUTILITY_MARGIN[depth] <= alpha) {
                applyFutilityPruning = true;
            }
        }
        int max = alpha;

        int bestMove = 0;

        pvArray.reset(ply);

        try (MoveList moves = searchContext.generateMoves(ply, color)) {

            sortMoves(ply, depth, color, moves, searchContext.getBoard());

            boolean firstChild = true;

            int searchedMoves = 0;
            for (MoveCursor moveCursor : moves) {
                searchContext.doMove(moveCursor);

                // skip illegal moves:
                if (searchContext.isInCheck(color)) {
                    searchContext.undoMove(moveCursor);
                    continue;
                }

                if (moveCursor.getOrder() > OrderCalculator.HISTORY_SCORE + 1000) {
                    /* late move pruning */
                    if (depth <= 4
                            && searchedMoves >= depth * 3 + 3
                            && !moveCursor.isCapture()
                            && !moveCursor.isPawnPromotion()
                            //                        && moveCursor.getOrder() > OrderCalculator.KILLER_SCORE
                            && !searchContext.isInCheck(color.invert())
                    ) {
                        searchContext.undoMove(moveCursor);
                        continue;
                    }
                }

                /**********************************************************************
                 *  When the futility pruning flag is set, prune moves which do not    *
                 *  give  check and do not change material balance.  Some  programs    *
                 *  prune insufficient captures as well, but that seems too risky.     *
                 **********************************************************************/

                if (applyFutilityPruning
                        && searchedMoves > 0
                        && !moveCursor.isCapture()
                        && !moveCursor.isPawnPromotion()
//                        && moveCursor.getOrder() > OrderCalculator.KILLER_SCORE
                        && !searchContext.isInCheck(color.invert())) {
                    searchContext.undoMove(moveCursor);
                    continue;
                }

                searchedMoves++;

                /**
                 * Late move reduction
                 */
                int R = determineLateMoveReduction(searchedMoves, depth, moveCursor, areWeInCheck);

                boolean redo = false;
                int score;
                do {
                    if (firstChild) {
                        /**
                         * do full search (for pvs search on the first move, or if pvs search is deactivated)
                         */
                        score = -negaMaximize(ply + 1, depth - 1 - R, color.invert(), -beta, -max);
                        if (doPVSSearch) {
                            firstChild = false;
                        }
                    } else {
                        // pvs try 0 window
                        score = -negaMaximize(ply + 1, depth - 1 - R, color.invert(), -max - 1, -max);

                        /**
                         * do a full window search in pvs search if score is out of our max, beta window:
                         */
                        if (max < score && score < beta) {
                            score = -negaMaximize(ply + 1, depth - 1 - R, color.invert(), -beta, -max);
                        }
                    }

                    /**********************************************************************
                     *  Sometimes reduced search brings us above alpha. This is unusual,   *
                     *  since we expected reduced move to be bad in first place. It is     *
                     *  not certain now, so let's search to the full, unreduced depth.     *
                     **********************************************************************/
                    redo = false;
                    if (R > 0 && score > max) {
                        R = 0;
                        redo = true;
                    }
                } while (redo);

                /** save score for all root moves: */
                searchContext.updateRootMoveScore(depth, moveCursor.getMoveInt(), score);

                searchContext.undoMove(moveCursor);
                if (score > max) {
                    max = score;

                    bestMove = moveCursor.getMoveInt();

//                    if (debug) {
//                        System.out.println("ply:" + ply + " pv:" + new MoveImpl(bestMove).toStr() + " score:" + score);
//                    }

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

            if (searchedMoves == 0) {
                return determineCheckMateOrPatt(ply, areWeInCheck);
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
    private int determineLateMoveReduction(int searchedMoves, int depth, MoveCursor moveCursor,
            boolean areWeInCheck) {
        if (useLateMoveReductions &&
                searchedMoves > LMR_AFTER_N_SEARCHED_MOVES &&
                depth > 3 &&
                !moveCursor.isCapture() &&
                !moveCursor.isPawnPromotion() &&
                //                moveCursor.getFigureType() != FigureType.Pawn.figureCode &&
                moveCursor.getOrder() > OrderCalculator.KILLER_SCORE &&
                !areWeInCheck) {

            if (searchedMoves > LMR_N_MOVES_REDUCE_MORE) {
                return 2;
            } else {
                return 1;
            }

        } else {
            return 0;
        }
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
            // todo should we store values from quiescence? and with what depth?
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

//        int alphaStart = alpha;

        boolean areWeInCheck = searchContext.isInCheck(color);

        Color opponent = color.invert();

        try (MoveList moves = searchContext.generateMoves(ply, color)) {
            quiescenceNodesVisited++;
            searchContext.adjustSelDepth(depth);

            // sort just by MMV-LVA, as we have no pv infos currently in quiescence...
            sortMoves(ply, depth, color, moves, searchContext.getBoard());

            int searchedMoves = 0;
            /* loop through the capture moves */
            for (MoveCursor moveCursor : moves) {
                searchContext.doMove(moveCursor);

                // skip illegal moves:
                if (searchContext.isInCheck(color)) {
                    searchContext.undoMove(moveCursor);
                    continue;
                }

                // skip low promotions
                if (moveCursor.isPawnPromotion()
                        && moveCursor.getPromotedFigure().figureType != FigureType.Queen) {
                    searchContext.undoMove(moveCursor);
                    continue;
                }

                searchedMoves++;

                if (moveCursor.isCapture()
                        || moveCursor.isPawnPromotion()
                        || searchContext.isInCheck(opponent)) {

                    /**********************************************************************
                     *  Delta cutoff - a move guarentees the score well below alpha, so    *
                     *  there's no point in searching it. We don't use his heuristic in    *
                     *  the endgame, because of the insufficient material issues.          *
                     **********************************************************************/

                    if (deltaCutOff
                            && !moveCursor.isPawnPromotion()
                            && x + see.pieceVal(moveCursor.getCapturedFigure()) + 200 < alpha
                            && searchContext.isOpeningOrMiddleGame()
                    ) {
                        searchContext.undoMove(moveCursor);
                        continue;
                    }

                    //Do not search moves with negative SEE values
                    //                    boolean seeGood =
                    //                            moveCursor.getOrder() >= GOOD_CAPT_LOWER && moveCursor.getOrder() <= GOOD_CAPT_UPPER;
                    //                    if (x > alphaStart
                    //                            && !moveCursor.isPawnPromotion()
                    //                            && searchContext.isOpeningOrMiddleGame()
                    //                            && !seeGood) {
                    //                        searchContext.undoMove(moveCursor);
                    //                        continue;
                    //                    }

                    x = -quiesce(ply + 1, depth - 1, color.invert(), -beta, -alpha);
                    if (x > alpha) {
                        if (x >= beta) {
                            searchContext.undoMove(moveCursor);
                            return beta;
                        }
                        alpha = x;
                    }
                }

                searchContext.undoMove(moveCursor);
            }

            if (searchedMoves == 0) {
                return determineCheckMateOrPatt(ply, areWeInCheck);
            }
        }

        // todo store values from quiescence? and if so, with what depth?
        searchContext.storeTT(color, x, alpha, beta, depth, 0);

        return alpha;
    }

    /**
     * Sorts the move list by calculating the move order first.
     *  @param depth
     * @param color
     * @param moves
     * @param board
     */
    private void sortMoves(int ply, int depth, Color color, MoveList moves,
            BoardRepresentation board) {
        int hashMove = searchContext.probeTTHashMove(color, depth);
        orderCalculator.prepareOrder(color, hashMove, ply, depth, board);
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
    public NegaMaxResult searchWithScore(SearchThreadContext stc, GameState gameState,
            GameContext context,
            int depth,
            int alpha, int beta, long stopTime, OrderHints orderHints) {

        this.orderCalculator = new OrderCalculator(orderHints, depth);

        searchContext = new SearchContext(stc, gameState, context, orderCalculator, evaluate, depth, alpha);

        this.stopTime = stopTime;

        extensionCounter = 0;

        pvArray.reset();

        initContext(context);

        int directScore = negaMaximize(1, depth, gameState.getWho2Move(), alpha, beta);

        List<Integer> pvMoves = expandPv
                ? enrichPVList(pvArray.getPvMoves(), gameState, context.getTtCache(), depth)
                : pvArray.getPvMoves();

        return new NegaMaxResult(directScore,
                pvMoves, searchContext, nodesVisited,
                quiescenceNodesVisited);

    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    @Override
    public void collectStatistics(Map stats) {

        Map rslts = new LinkedHashMap();
        stats.put("negamax alpha/beta", rslts);

        rslts.put("nodesVisited", nodesVisited);

        if (maxQuiescenceDepth > 0) {
            rslts.put("quiescenceNodesVisited", quiescenceNodesVisited);
        }
        rslts.put("cutoff", cutOff);

        Map searchstatsMap = new LinkedHashMap();
        searchContext.collectStatistics(searchstatsMap);
        rslts.put("search", searchstatsMap);

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

    public static boolean canRefineEval(final TTEntry tte, final int eval) {
        if (tte != null) {
            int score = tte.getValue();
            if (tte.isExact() || tte.isUpperBound() && score < eval || tte.isLowerBound() && score > eval) {
                return true;
            }
        }
        return false;
    }
}
