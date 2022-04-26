package org.mattlang.jc.engine.search;

import static java.lang.Math.abs;
import static org.mattlang.jc.Constants.MAX_PLY;
import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;
import static org.mattlang.jc.movegenerator.MoveGenerator.GenMode.NORMAL;
import static org.mattlang.jc.movegenerator.MoveGenerator.GenMode.QUIESCENCE;

import java.util.LinkedHashMap;
import java.util.List;
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
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.see.SEE;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.movegenerator.MoveGenerator.GenMode;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.util.MoveValidator;

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

    private EvaluateFunction evaluate;

    private int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    private boolean doChessExtension = Factory.getDefaults().getConfig().chessExtension.getValue();
    private boolean expandPv = Factory.getDefaults().getConfig().expandPv.getValue();
    private boolean mateDistancePruning = Factory.getDefaults().getConfig().mateDistancePruning.getValue();
    private boolean iid = Factory.getDefaults().getConfig().internalIterativeDeepening.getValue();

    private PVTriangularArray pvArray = new PVTriangularArray();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int quiescenceNodesVisited = 0;

    private int extensionCounter = 0;

    private int cutOff;

    private boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();

    private boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();
    private boolean staticNullMove = Factory.getDefaults().getConfig().staticNullMove.getValue();
    private boolean razoring = Factory.getDefaults().getConfig().razoring.getValue();
    private boolean deltaCutOff = Factory.getDefaults().getConfig().deltaCutoff.getValue();
    private boolean useLateMoveReductions = Factory.getDefaults().getConfig().useLateMoveReductions.getValue();
    private boolean futilityPruning = Factory.getDefaults().getConfig().futilityPruning.getValue();

    private int[] parentMoves=new int[MAX_PLY];

    private ExtensionsInfo extensionsInfo = new ExtensionsInfo();

    private SearchContext searchContext;

    private boolean debug = true;

    private MoveValidator moveValidator = new MoveValidator();

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

        searchWithScore(SearchThreadContexts.CONTEXTS.getContext(0), gameState, context, depth,
                ALPHA_START, BETA_START,
                stopTime, OrderHints.NO_HINTS);
        return new MoveImpl(searchContext.getSavedMove());
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

        /**
         * Return immediately if it is a repetition or draw by material.
         * By draw by material we only immediately return on higher plys because otherwise we would not return a move.
         */
        if (searchContext.isRepetition() || (searchContext.isDrawByMaterial() && ply !=1)) {
            return Weights.REPETITION_WEIGHT;
        }

        boolean areWeInCheck = searchContext.isInCheck(color);
        depth = checkToExtend(areWeInCheck, color, depth);

        if (depth == 0) {
            return quiesce(ply + 1, -1, color, alpha, beta);
        }

        TTResult tte = searchContext.getTTEntry();
        if (tte != null && tte.getDepth() >= depth && ply != 1) {
            if (tte.isExact()) // stored value is exact
                return adjustScore(tte.getScore(), ply);
            if (tte.isLowerBound() && tte.getScore() > alpha)
                alpha = adjustScore(tte.getScore(), ply); // update lowerbound alpha if needed
            else if (tte.isUpperBound() && tte.getScore() < beta)
                beta = adjustScore(tte.getScore(), ply); // update upperbound beta if needed
            if (alpha >= beta)
                return adjustScore(tte.getScore(), ply); // if lowerbound surpasses upperbound
        }

        checkTimeout();

        boolean applyFutilityPruning = false;

        if (not_pv && !areWeInCheck) {

            int staticEval = searchContext.eval(color);

            // use tt value as eval if possible
            if (canRefineEval(tte, staticEval)) {
                staticEval = tte.getScore();
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

        int parentMove = ply <= 1 ? 0 : parentMoves[ply - 1];

        if (iid) {
            doInternalIterativeDeepening(ply, depth, color, alpha, beta, not_pv, areWeInCheck);
        }

        try (MoveBoardIterator moveCursor = searchContext.genSortedMovesIterator(NORMAL, ply, color, parentMove)) {

            boolean firstChild = true;

            int currSearchedNodes = nodesVisited;

            int searchedMoves = 0;

            while (moveCursor.doNextMove()) {

                parentMoves[ply] = moveCursor.getMoveInt();

                // late move pruning does not bring any benefit so far...
                //                if (applyFutilityPruning && moveCursor.getOrder() > OrderCalculator.HISTORY_SCORE + 1000) {
                //                    /* late move pruning */
                //                    if (depth <= 4
                //                            && searchedMoves >= depth * 4 + 3
                //                            && !moveCursor.isCapture()
                //                            && !moveCursor.isPawnPromotion()
                //                            //                        && moveCursor.getOrder() > OrderCalculator.KILLER_SCORE
                //                            && !searchContext.isInCheck(color.invert())
                //                    ) {
                //                        searchContext.undoMove(moveCursor);
                //                        continue;
                //                    }
                //                }
//                boolean isHashMove = tte != null && tte.getMove() == moveCursor.getMoveInt();
//                boolean moveIsPrunable = searchedMoves > 0
//                        && !isHashMove
//                        && !moveCursor.isCapture()
//                        && !moveCursor.isPromotion()
//                        && !areWeInCheck
//                        && !searchContext.isInCheck(color.invert());

                /**********************************************************************
                 *  When the futility pruning flag is set, prune moves which do not    *
                 *  give  check and do not change material balance.  Some  programs    *
                 *  prune insufficient captures as well, but that seems too risky.     *
                 **********************************************************************/

                if (applyFutilityPruning
                        && searchedMoves > 0
                        && !moveCursor.isCapture()
                        && !moveCursor.isPromotion()
//                        && moveCursor.getOrder() > OrderCalculator.KILLER_SCORE
                        && !searchContext.isInCheck(color.invert())) {
                    continue;
                }

                // Futility pruning using SEE
//                int pruneDepth = Math.max(0, depth - 3); // shortcut, maybe fine tune this...
//
//                if (moveIsPrunable
//                        && pruneDepth <= 6
//                        && !see.see_ge(searchContext.getBoard(), moveCursor, -24 * pruneDepth * pruneDepth))
//                    continue;

//                boolean isHashMove = tte != null && tte.getMove() == moveCursor.getMoveInt();
//
//                if (moveIsPrunable
//                        && not_pv
//                        && !isHashMove
//                        && max > -32000 && max > alpha
//                        && depth <= 5
//                        && !see.see_ge(searchContext.getBoard(), moveCursor, -100 * depth))
//                    continue;

                searchedMoves++;

                /**
                 * Late move reduction
                 */
                int r = determineLateMoveReduction(searchedMoves, depth, moveCursor, areWeInCheck);

                boolean redo = false;
                int score;
                do {
                    if (firstChild) {
                        /**
                         * do full search (for pvs search on the first move, or if pvs search is deactivated)
                         */
                        score = -negaMaximize(ply + 1, depth - 1 - r, color.invert(), -beta, -max);
                        if (doPVSSearch) {
                            firstChild = false;
                        }
                    } else {
                        // pvs try 0 window
                        score = -negaMaximize(ply + 1, depth - 1 - r, color.invert(), -max - 1, -max);

                        /**
                         * do a full window search in pvs search if score is out of our max, beta window:
                         */
                        if (max < score && score < beta) {
                            score = -negaMaximize(ply + 1, depth - 1 - r, color.invert(), -beta, -max);
                        }
                    }

                    /**********************************************************************
                     *  Sometimes reduced search brings us above alpha. This is unusual,   *
                     *  since we expected reduced move to be bad in first place. It is     *
                     *  not certain now, so let's search to the full, unreduced depth.     *
                     **********************************************************************/
                    redo = false;
                    if (r > 0 && score > max) {
                        r = 0;
                        redo = true;
                    }
                } while (redo);

                /** save score for all root moves: */
                searchContext.updateRootMoveScore(depth, moveCursor.getMoveInt(), score, nodesVisited-currSearchedNodes);

                if (score > max) {
                    max = score;

                    bestMove = moveCursor.getMoveInt();

                    pvArray.set(bestMove, ply);
                    searchContext.savePv(bestMove);
                    searchContext.updateRootBestMove(depth, bestMove, score);

                    if (max >= beta) {
//                        if (!areWeInCheck) {
                            searchContext.updateCutOffHeuristics(ply, depth, color, parentMove, bestMove, moveCursor);
                        //                        }
                        cutOff++;
                        break;
                    }

                }

                // update "bad" heuristic
                searchContext.updateBadHeuristic(depth, color, moveCursor);
            }

            if (searchedMoves == 0) {
                return determineCheckMateOrPatt(ply, areWeInCheck);
            }
        }

        // save score and best move info in tt:
        searchContext.storeTT(color, max, alpha, beta, depth, bestMove);

        return max;
    }

    private void doInternalIterativeDeepening(int ply, int depth, Color color, int alpha, int beta, boolean not_pv,
            boolean areWeInCheck) {
        boolean is_pv = !not_pv;
        int hashMove = searchContext.probeTTHashMove();
        boolean hasPvMove = searchContext.hasPvMove(ply);
        // Internal iterative deepening
        // When there is no hash move available, it is sometimes worth doing a
        // shallow search to try and look for one
        // This is especially true at PV nodes and potential cut nodes
        if (hashMove == 0 && !hasPvMove && !areWeInCheck
                && ((is_pv && depth >= 7)
                || (not_pv && depth >= 8))) {
            int iidDepth = is_pv ? depth - depth / 2 - 1 : (depth - 2) / 2;
            negaMaximize(ply, iidDepth, color, alpha, beta);

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
                !moveCursor.isPromotion() &&
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

        if (searchContext.isRepetition() || searchContext.isDrawByMaterial()) {
            return Weights.REPETITION_WEIGHT;
        }

        TTResult tte = searchContext.getTTEntry();
        if (tte != null && tte.getDepth() >= depth && ply != 1) {
            if (tte.isExact()) // stored value is exact
                return adjustScore(tte.getScore(), ply);
            if (tte.isLowerBound() && tte.getScore() > alpha)
                alpha = adjustScore(tte.getScore(), ply); // update lowerbound alpha if needed
            else if (tte.isUpperBound() && tte.getScore() < beta)
                beta = adjustScore(tte.getScore(), ply); // update upperbound beta if needed
            if (alpha >= beta)
                return adjustScore(tte.getScore(), ply); // if lowerbound surpasses upperbound
        }

        int eval = searchContext.eval(color);

        /* are we too deep? */
        if (depth < -maxQuiescenceDepth) {
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

        //        boolean areWeInCheck = searchContext.isInCheck(color);

        //        Color opponent = color.invert();

        // todo: we have no "evations" mode in move generation for now. therefore we must distinquish
        // if we are in check and then use the normal mode instead of only capture generation:
        GenMode moveGenMode = /*areWeInCheck? NORMAL:*/ QUIESCENCE;

        try (MoveBoardIterator moveCursor = searchContext.genSortedMovesIterator(moveGenMode, ply, color, 0)) {
            quiescenceNodesVisited++;
            searchContext.adjustSelDepth(depth);

            int searchedMoves = 0;
            /* loop through the capture moves */

            while (moveCursor.doNextMove()) {

                // skip low promotions
                if (moveCursor.isPromotion()
                        && moveCursor.getPromotedFigure().figureType != FigureType.Queen) {
                    continue;
                }

                //                searchedMoves++;

                if (moveCursor.isCapture()
                        || moveCursor.isPromotion()) {

                    /**********************************************************************
                     *  Delta cutoff - a move guarentees the score well below alpha, so    *
                     *  there's no point in searching it. We don't use his heuristic in    *
                     *  the endgame, because of the insufficient material issues.          *
                     **********************************************************************/

                    if (deltaCutOff
                            && !moveCursor.isPromotion()
                            && x + see.pieceVal(moveCursor.getCapturedFigure()) + 200 < alpha
                            && searchContext.isOpeningOrMiddleGame()
                    ) {
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
                            return beta;
                        }
                        alpha = x;
                    }
                }

            }
            
        }

        return alpha;
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

        searchContext = new SearchContext(stc, gameState, context, orderHints, evaluate, depth, alpha);

        this.stopTime = stopTime;

        extensionCounter = 0;

        pvArray.reset();

        int directScore = negaMaximize(1, depth, gameState.getWho2Move(), alpha, beta);

        List<Integer> pvMoves = expandPv
                ? moveValidator.enrichPVList(pvArray.getPvMoves(), gameState, stc.getPvCache(), depth)
                : pvArray.getPvMoves();

        NegaMaxResult rslt = new NegaMaxResult(directScore,
                pvMoves, searchContext, nodesVisited,
                quiescenceNodesVisited);

        stc.setOrderHints(new OrderHints(rslt, stc,true));

        return rslt;
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

    public static boolean canRefineEval(final TTResult tte, final int eval) {
        if (tte != null) {
            int score = tte.getScore();
            if (tte.isExact() || tte.isUpperBound() && score < eval || tte.isLowerBound() && score > eval) {
                return true;
            }
        }
        return false;
    }
}
