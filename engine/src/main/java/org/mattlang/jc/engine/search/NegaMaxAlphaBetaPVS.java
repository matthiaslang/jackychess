package org.mattlang.jc.engine.search;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.logging.Level.FINE;
import static org.mattlang.jc.Constants.MAX_PLY;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.engine.evaluation.Weights.*;
import static org.mattlang.jc.engine.sorting.OrderCalculator.*;
import static org.mattlang.jc.movegenerator.GenMode.NORMAL;
import static org.mattlang.jc.movegenerator.GenMode.QUIESCENCE;
import static org.mattlang.jc.moves.MoveListToStringConverter.movedescr;
import static org.mattlang.jc.moves.MoveToStringConverter.toLongAlgebraic;

import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.see.SEE;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.util.MoveValidator;

import lombok.Getter;

/**
 * Negamax with Alpha Beta Pruning. Supports PVS Search which could be optional activated.
 * Supports TT Cache
 */
public final class NegaMaxAlphaBetaPVS implements AlphaBetaSearchMethod {

    private static final Logger LOGGER = Logger.getLogger(NegaMaxAlphaBetaPVS.class.getSimpleName());

    private static final int[] RAZORING_MARGIN = { 0, 240, 280, 300 };

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;


    private static final int[] STATIC_NULLMOVE_MARGIN = { 0, 60, 130, 210, 300, 400, 510 };
    private static final int[] FUTILITY_MARGIN = { 0, 80, 170, 270, 380, 500, 630 };

    /**
     * predefined LMR reductions per depth and performed moves.
     */
    private static final int[][] LMR_TABLE = new int[64][64];
    public static final int UPDATE_INTERVAL = 1000 * 3;
    private static final int MAX_QUIESCENCE_TT_PLY = 64;

    static {
        // Ethereal LMR formula with depth and number of performed moves
        for (int depth = 1; depth < 64; depth++) {
            for (int moveNumber = 1; moveNumber < 64; moveNumber++) {
                LMR_TABLE[depth][moveNumber] = (int) (0.6f + Math.log(depth) * Math.log(moveNumber * 1.2f) / 2.5f);
            }
        }
    }

    private final int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    private final boolean doChessExtension = Factory.getDefaults().getConfig().chessExtension.getValue();
    private final boolean mateDistancePruning = Factory.getDefaults().getConfig().mateDistancePruning.getValue();
    private final boolean iid = Factory.getDefaults().getConfig().internalIterativeDeepening.getValue();

    private final PVTriangularArray pvArray = new PVTriangularArray();

    private long stopTime = 0;

    private long nextUpdateTime = 0;

    private SearchListener searchListener;

    private final boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();

    private final boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();
    private final boolean staticNullMove = Factory.getDefaults().getConfig().staticNullMove.getValue();
    private final boolean razoring = Factory.getDefaults().getConfig().razoring.getValue();
    private final boolean deltaCutOff = Factory.getDefaults().getConfig().deltaCutoff.getValue();
    private final boolean useLateMoveReductions = Factory.getDefaults().getConfig().useLateMoveReductions.getValue();
    private final boolean futilityPruning = Factory.getDefaults().getConfig().futilityPruning.getValue();

    /**
     * parent moves. needs one more place than max ply to save the "following" move.
     */
    private final int[] parentMoves = new int[MAX_PLY + 1];

    private SearchContext searchContext;

    private final MoveValidator moveValidator = new MoveValidator();

    @Getter
    private final SearchStatistics statistics = new SearchStatistics();

    private boolean isWorker = false;

    public NegaMaxAlphaBetaPVS() {
        reset();
    }

    @Override
    public Move search(GameState gameState, GameContext context, int depth) {
        assert depth > 0;
        reset();

        searchWithScore(SearchThreadContexts.CONTEXTS.getContext(0), gameState, context, depth,
                ALPHA_START, BETA_START,
                stopTime);
        return new MoveImpl(searchContext.getSavedMove());
    }

    public void resetStatistics() {
        statistics.resetStatistics();
    }

    public void reset() {
        resetStatistics();
    }

    private int negaMaximize(final int ply, final int depth, final Color color,
            int alpha, int beta) {

        /**
         * Return immediately if it is a repetition or draw by material.
         * By draw by material we only immediately return on higher plys because otherwise we would not return a move.
         */

        if (searchContext.isDrawByMaterial() && ply != 1) {
            statistics.drawByMaterialDetected++;
            return Weights.REPETITION_WEIGHT;
        }

        if (searchContext.isRepetition() && ply != 1) {
            statistics.drawByRepetionDetected++;
            return searchContext.evaluateRepetition(color);
        }

        final int mateValue = KING_WEIGHT - ply;

        statistics.nodesVisited++;
        final boolean not_pv = abs(beta - alpha) <= 1;

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
            if (alpha >= beta) {
                statistics.mateDistancePruningCount++;
                return alpha;
            }
        }

        if (depth == 0) {
            return quiesce(ply + 1, -1, color, alpha, beta);
        }

        int hashMove = 0;

        TTResult tte = searchContext.getTTEntry();
        if (tte != null) {
            if (tte.getDepth() >= depth && ply != 1) {
                if (tte.isExact()) {// stored value is exact
                    statistics.ttPruningCount++;
                    return tte.getAdjustedScore(ply);
                } else if (tte.isLowerBound() && tte.getScore() > alpha)
                    alpha = tte.getAdjustedScore(ply); // update lowerbound alpha if needed
                else if (tte.isUpperBound() && tte.getScore() < beta)
                    beta = tte.getAdjustedScore(ply); // update upperbound beta if needed
                if (alpha >= beta) {
                    statistics.ttPruningCount++;
                    return tte.getAdjustedScore(ply); // if lowerbound surpasses upperbound
                }
            }

            hashMove = tte.getMove();
        }

        checkTimeout();

        // Reset killers of children to keep them local to similar positions
        searchContext.resetKillers(ply + 1);

        boolean applyFutilityPruning = false;

        final boolean areWeInCheck = searchContext.isInCheck(color);

        final boolean pruneable = not_pv && !areWeInCheck;

        if (pruneable) {

            int staticEval = searchContext.eval(color);

            // use tt value as eval if possible
            if (canRefineEval(tte, staticEval)) {
                staticEval = tte.getScore();
            }

            /**************************************************************************
             * EVAL PRUNING / STATIC NULL MOVE                                         *
             **************************************************************************/

            if (staticNullMove
                    && depth < STATIC_NULLMOVE_MARGIN.length
                    && abs(beta - 1) > ALPHA_START + 100) {

                int eval_margin = STATIC_NULLMOVE_MARGIN[depth];
                if (staticEval - eval_margin >= beta) {
                    statistics.staticNullMovePruningCount++;
                    return staticEval;
                }
            }

            /**
             * null move reduction:
             * only, for non pv nodes,
             * and no null move has already been chosen in this row
             * and we have non pawn material (because of zugzwang issues)
             * and we are not in check (also for zugzwang)
             */
            if (useNullMoves &&
                    depth > 2 &&
                    searchContext.getNullMoveCounter() == 0 &&
                    searchContext.isNoZugzwang()
            ) {
                int R = (depth > 6) ? 3 : 2;

                searchContext.doPrepareNullMove();
                int eval = -negaMaximize(ply + 1, depth - R, color.invert(), -beta, -beta + 1);
                searchContext.undoNullMove();
                statistics.nullMoveTryCount++;
                if (eval >= beta) {
                    statistics.nullMovePruningCount++;
                    if (depth >= 10) {
                        // do verification at high depths:
                        int verifyScore = negaMaximize(ply + 1, depth - R, color.invert(), beta + 1, beta);
                        searchContext.resetNullMoveCounter();
                        if (verifyScore >= beta)
                            return verifyScore;
                    } else {
                        searchContext.resetNullMoveCounter();
                        return eval;
                    }
                }
                searchContext.resetKillers(ply + 1);
            }

            // ProbCut
            // If a winning capture scores much higher than beta on a shallow search,
            // then we can assume a beta cutoff would happen on the full search as
            // well, and return early.
            // Idea from Stockfish
            if (depth >= 6 && staticEval >= beta - 100 - 20 * depth
                    && abs(beta) < VALUE_TB_WIN_IN_MAX_PLY) {
                int probCutMargin = beta + 90;
                int probCutCount = 0;
                try (MoveBoardIterator moveCursor = searchContext.genSortedMovesIterator(QUIESCENCE, ply, color,
                        hashMove, 0,
                        probCutMargin - staticEval)) {
                    while (moveCursor.nextMove() && probCutCount < 3) {
                        if (moveCursor.doValidMove()) {
                            probCutCount++;
                            if (moveCursor.getMoveInt() != hashMove) {
                                int score =
                                        -negaMaximize(ply + 1, depth - depth / 4 - 4, color.invert(), -probCutMargin,
                                                -probCutMargin + 1);
                                if (score >= probCutMargin)
                                    return score;
                            }
                        }
                    }
                }
            }

            /**************************************************************************
             *  RAZORING - if a node is close to the leaf and its static score is low, *
             *  we drop directly to the quiescence search.                             *
             **************************************************************************/
            if (razoring
                    && tte == null
                    && searchContext.getNullMoveCounter() == 0
                    && noPawnPromotions(searchContext.getBoard()) // no pawns to promote in one move
                    && depth < RAZORING_MARGIN.length
                    && Math.abs(alpha) < KING_WEIGHT) {
                int razorMarginOfDepth = RAZORING_MARGIN[depth];
                if (staticEval + razorMarginOfDepth < alpha) {
                    statistics.razoringTryCount++;
                    int val = quiesce(ply + 1, -1, color, alpha - razorMarginOfDepth, alpha - razorMarginOfDepth + 1);
                    if (val + razorMarginOfDepth <= alpha) {
                        statistics.razoringPruningCount++;
                        return val;
                    }
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
                    depth < FUTILITY_MARGIN.length
                    && abs(alpha) < 9000
                    && staticEval + FUTILITY_MARGIN[depth] <= alpha) {
                applyFutilityPruning = true;
            }
        }
        int max = alpha;

        int bestMove = 0;

        pvArray.reset(ply);

        int parentMove = ply <= 1 ? 0 : parentMoves[ply - 1];

        if (iid && hashMove == 0) {
            doInternalIterativeDeepening(ply, depth, color, alpha, beta, not_pv, areWeInCheck);
            hashMove = searchContext.probeTTHashMove();
        }

        try (MoveBoardIterator moveCursor = searchContext.genSortedMovesIterator(NORMAL, ply, color, hashMove,
                parentMove, 0)) {

            boolean firstChild = true;

            int searchedMoves = 0;

            int lastorder = Integer.MIN_VALUE;

            if (BuildConstants.ASSERTIONS) {
                LOGGER.fine("ply: " + ply + " depth: " + depth + " hashmove: " + hashMove + " start traversion");
            }

            while (moveCursor.nextMove()) {

                if (BuildConstants.ASSERTIONS) {
                    LOGGER.fine("ply: " + ply + " depth: " + depth + " traversing move " + movedescr(moveCursor));
                    if (lastorder > moveCursor.getOrder() && lastorder != QUEEN_PROMOTION_SCORE
                            && moveCursor.getOrder() != QUEEN_PROMOTION_SCORE) {
                        throw new IllegalStateException(
                                "last order: " + lastorder + " > curr order: " + moveCursor.getOrder());
                    }
                    lastorder = moveCursor.getOrder();
                }

                parentMoves[ply] = moveCursor.getMoveInt();

                /**********************************************************************
                 *  When the futility pruning flag is set, prune moves which do not    *
                 *  give  check and do not change material balance.  Some  programs    *
                 *  prune insufficient captures as well, but that seems too risky.     *
                 **********************************************************************/

                final boolean quietMove = !moveCursor.isCapture() && !moveCursor.isPromotion();

                // todo the condition that it gives check is now out-commented...
                if (applyFutilityPruning
                        && searchedMoves > 0
                        && quietMove
                    //                        && moveCursor.getOrder() > OrderCalculator.KILLER_SCORE
                    /*&& !searchContext.isInCheck(color.invert())*/) {
                    statistics.futilityPruningCount++;
                    continue;
                }

                // todo additional checks: no discovering move...
                // && !cb.isDiscoveredMove(MoveUtil.getFromIndex(move)), order rel. below...?
                if (pruneable && searchedMoves > 0 && !OrderCalculator.isRelevantMove(moveCursor.getOrder())) {
                    /* late move pruning */
                    if (quietMove
                            && depth <= 4
                            && searchedMoves >= depth * 3 + 3) {
                        statistics.lateMovePruningCount++;
                        continue;
                    }
                    /** SEE Pruning*/
                    if (moveCursor.isCapture() && !moveCursor.isPromotion() && depth <= 6
                            && SEE.see_ge(searchContext.getBoard(), moveCursor, -20 * depth * depth)) {
                        statistics.seePruningCount++;
                        continue;
                    }
                }

                if (moveCursor.doValidMove()) {

                    searchedMoves++;

                    /**
                     * Late move reduction
                     */
                    final int r = determineLateMoveReduction(searchedMoves, depth, moveCursor, areWeInCheck, not_pv);

                    // currently we do not support any extensions
                    final int extension = 0;
                    int score;

                    if (firstChild) {
                        /**
                         * do full search (for pvs search on the first move, or if pvs search is deactivated)
                         */
                        score = -negaMaximize(ply + 1, depth - 1 + extension, color.invert(), -beta, -max);
                        if (doPVSSearch) {
                            firstChild = false;
                        }
                    } else {
                        // pvs try 0 window
                        score = -negaMaximize(ply + 1, depth - 1 - r + extension, color.invert(), -max - 1, -max);

                        // research if the reduced search did not fail low
                        if (r > 0 && score > max) {
                            score = -negaMaximize(ply + 1, depth - 1 + extension, color.invert(), -max - 1, -max);
                        }

                        /**
                         * do a full window search in pvs search if score is out of our max, beta window:
                         */
                        if (max < score && score < beta) {
                            score = -negaMaximize(ply + 1, depth - 1 + extension, color.invert(), -beta, -max);
                        }
                    }

                    if (score > max) {
                        max = score;

                        bestMove = moveCursor.getMoveInt();

                        if (BuildConstants.ASSERTIONS) {
                            LOGGER.fine("ply: " + ply + " depth: " + depth + " found new bestmove: "
                                    + toLongAlgebraic(moveCursor) + " score: " + max);
                        }

                        pvArray.set(bestMove, ply);
                        searchContext.updateRootBestMove(depth, bestMove, score);

                        if (max >= beta) {
                            if (BuildConstants.ASSERTIONS) {
                                LOGGER.fine("ply: " + ply + " depth: " + depth + " found cut off: "
                                        + toLongAlgebraic(moveCursor) + " score: " + max + " beta: " + beta);
                            }

                            if (!areWeInCheck) {
                                searchContext.updateCutOffHeuristics(ply, depth, color, parentMove,
                                        bestMove, moveCursor);
                            }
                            statistics.countCutOff(moveCursor, searchedMoves);
                            break;
                        }

                    }

                    // update "bad" heuristic
                    if (!areWeInCheck) {
                        searchContext.updateBadHeuristic(depth, color, moveCursor);
                    }
                }
            }
            statistics.noCutOffFoundCount++;

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
        // Internal iterative deepening
        // When there is no hash move available, it is sometimes worth doing a
        // shallow search to try and look for one
        // This is especially true at PV nodes and potential cut nodes
        if (!areWeInCheck
                && ((is_pv && depth >= 7)
                || (not_pv && depth >= 8))) {
            statistics.iterativeDeepeningCount++;
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
            boolean areWeInCheck, boolean not_pv) {
        if (useLateMoveReductions &&
                searchedMoves > 1 &&
                depth > 2 &&
                !moveCursor.isCapture() &&
                !moveCursor.isPromotion() &&
                !isPawnPush(moveCursor) &&
                //                moveCursor.getFigureType() != FigureType.Pawn.figureCode &&
                //                moveCursor.getOrder() > OrderCalculator.KILLER_SCORE &&
                !areWeInCheck) {

            int reduction = LMR_TABLE[min(depth, 63)][min(searchedMoves, 63)];

            if (isHashMove(moveCursor.getOrder())
                    || isKillerMove(moveCursor.getOrder())
                    || isCounterMove(moveCursor.getOrder())) {
                reduction--;
            }
            if (not_pv) {
                reduction++;
            }
            if (reduction <= 0) {
                reduction = 0;
            }
            if (reduction > depth - 1) {
                reduction = depth - 1;
            }

            return reduction;

        } else {
            return 0;
        }
    }

    /**
     * Is the move a pawn push to rank7 or rank2 with option to promote quickly within the next move?
     *
     * @param moveCursor
     * @return
     */
    private boolean isPawnPush(MoveCursor moveCursor) {
        return moveCursor.getFigureType() == FT_PAWN && (moveCursor.getToIndex() > 47 || moveCursor.getToIndex() < 16);
    }

    private void checkTimeout() {
        if (stopTime != 0 && System.currentTimeMillis() > stopTime) {
            throw new TimeoutException();
        }
        if (Thread.interrupted()) {
            throw new TimeoutException();
        }
        if (!isWorker && searchListener != null && nextUpdateTime != 0 && System.currentTimeMillis() > nextUpdateTime) {
            updateListener();
            nextUpdateTime = System.currentTimeMillis() + UPDATE_INTERVAL;
        }
    }

    private void updateListener() {
        searchListener.updateCurrMove(searchContext.getSavedMove(),
                searchContext.getSavedMoveScore(),
                searchContext.getTargetDepth(),
                searchContext.getSelDepth(),
                getNodesVisited()
        );
    }

    private int quiesce(final int ply, final int depth, final Color color, int alpha, int beta) {
        statistics.nodesVisited++;

        if (searchContext.isDrawByMaterial()) {
            return Weights.REPETITION_WEIGHT;
        }

        if (searchContext.isRepetition()) {
            return searchContext.evaluateRepetition(color);
        }

        int hashMove = 0;
        final TTResult tte = searchContext.getTTEntry();
        if (tte != null) {
            hashMove = tte.getMove();
            if (tte.isExact()) {// stored value is exact
                statistics.ttPruningCount++;
                return tte.getAdjustedScore(MAX_QUIESCENCE_TT_PLY);
            }
            if (tte.isLowerBound() && tte.getScore() > alpha)
                alpha = tte.getAdjustedScore(MAX_QUIESCENCE_TT_PLY); // update lowerbound alpha if needed
            else if (tte.isUpperBound() && tte.getScore() < beta)
                beta = tte.getAdjustedScore(MAX_QUIESCENCE_TT_PLY); // update upperbound beta if needed
            if (alpha >= beta) {
                statistics.ttPruningCount++;
                return tte.getAdjustedScore(MAX_QUIESCENCE_TT_PLY); // if lowerbound surpasses upperbound
            }

        }

        final int eval = searchContext.eval(color);

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

        int bestValue = x;
        final int futilityBase = x + 200;

        int movecount = 0;

        try (MoveBoardIterator moveCursor = searchContext.genSortedMovesIterator(QUIESCENCE, ply, color, hashMove, 0,
                0)) {
            statistics.quiescenceNodesVisited++;
            searchContext.adjustSelDepth(depth);

            /* loop through the capture moves */

            while (moveCursor.nextMove()) {

                // skip low promotions
                if (moveCursor.isPromotion()
                        && moveCursor.getPromotedFigure().figureType != FigureType.Queen) {
                    statistics.skipLowPromotionsCount++;
                    continue;
                }

                /**********************************************************************
                 *  Delta cutoff - a move guarentees the score well below alpha, so    *
                 *  there's no point in searching it. We don't use his heuristic in    *
                 *  the endgame, because of the insufficient material issues.          *
                 **********************************************************************/
                if (bestValue > VALUE_TB_LOSS_IN_MAX_PLY) {
                    if (!moveCursor.isPromotion()) {
                        if (movecount > 2) {
                            continue;
                        }

                        if (deltaCutOff
                                && futilityBase + SEE.pieceVal(moveCursor.getCapturedFigure()) < alpha
                                && searchContext.isOpeningOrMiddleGame()
                        ) {
                            statistics.deltaCutoffCount++;
                            continue;
                        }

                        if (futilityBase <= alpha && !SEE.see_ge(searchContext.getBoard(), moveCursor, 1)) {
                            statistics.deltaCutoffCount++;
                            continue;
                        }

                        // Do not search moves with bad enough SEE values
                        if (!SEE.see_ge(searchContext.getBoard(), moveCursor, -95))
                            continue;
                    }
                }

                if (moveCursor.doValidMove()) {
                    movecount++;
                    x = -quiesce(ply + 1, depth - 1, color.invert(), -beta, -alpha);
                    if (x > bestValue) {
                        bestValue = x;
                        if (x > alpha) {
                            if (x >= beta) {
                                return beta;
                            }
                            alpha = x;
                        }
                    }
                }

            }

        }

        return alpha;
    }

    @Override
    public NegaMaxResult searchWithScore(SearchThreadContext stc, GameState gameState,
            GameContext context,
            int depth,
            int alpha, int beta, long stopTime) {

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "negamax search depth {0} [{1} - {2}]", new Object[] { depth, alpha, beta });
        }
        searchContext = new SearchContext(stc, gameState, context, depth, alpha);

        this.stopTime = stopTime;
        this.nextUpdateTime = System.currentTimeMillis() + UPDATE_INTERVAL;

        pvArray.reset();

        int directScore = negaMaximize(1, depth, gameState.getWho2Move(), alpha, beta);

        List<Integer> pvMoves = moveValidator.validateAndCorrectPvList(pvArray.getPvMoves(), gameState);

        NegaMaxResult rslt = new NegaMaxResult(directScore,
                pvMoves, searchContext, statistics.nodesVisited,
                statistics.quiescenceNodesVisited);

        return rslt;
    }

    public int getNodesVisited() {
        return statistics.nodesVisited;
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

    /**
     * Returns true if the site to move does not have any pawn promotion moves (probably).
     *
     * @param board
     * @return
     */
    private static boolean noPawnPromotions(BoardRepresentation board) {
        return board.getSiteToMove() == Color.WHITE && (board.getBoard().getPawns(nWhite) & BB.rank7) == 0
                || board.getSiteToMove() == Color.BLACK
                && (board.getBoard().getPawns(nBlack) & BB.rank2) == 0;
    }

    public void setIsWorker(boolean isWorker) {
        this.isWorker = isWorker;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }
}
