package org.mattlang.jc.engine.search;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.logging.Level.FINE;
import static org.mattlang.jc.Constants.MAX_PLY;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;
import static org.mattlang.jc.engine.sorting.OrderCalculator.*;
import static org.mattlang.jc.movegenerator.MoveGenerator.GenMode.NORMAL;
import static org.mattlang.jc.movegenerator.MoveGenerator.GenMode.QUIESCENCE;

import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.see.SEE;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.movegenerator.MoveGenerator.GenMode;
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

    public static final int VALUE_TB_WIN_IN_MAX_PLY = KING_WEIGHT - 2 * MAX_PLY;
    public static final int VALUE_TB_LOSS_IN_MAX_PLY = -VALUE_TB_WIN_IN_MAX_PLY;

    private static final SEE see = new SEE();
    private static final int[] STATIC_NULLMOVE_MARGIN = { 0, 60, 130, 210, 300, 400, 510 };
    private static final int[] FUTILITY_MARGIN = { 0, 80, 170, 270, 380, 500, 630 };

    /** predefined LMR reductions per depth and performed moves.*/
    private static final int[][] LMR_TABLE = new int[64][64];

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
    private final  boolean iid = Factory.getDefaults().getConfig().internalIterativeDeepening.getValue();

    private final PVTriangularArray pvArray = new PVTriangularArray();

    private long stopTime = 0;

    private int extensionCounter = 0;

    private final boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();

    private final boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();
    private final boolean staticNullMove = Factory.getDefaults().getConfig().staticNullMove.getValue();
    private final boolean razoring = Factory.getDefaults().getConfig().razoring.getValue();
    private final boolean deltaCutOff = Factory.getDefaults().getConfig().deltaCutoff.getValue();
    private final boolean useLateMoveReductions = Factory.getDefaults().getConfig().useLateMoveReductions.getValue();
    private final boolean futilityPruning = Factory.getDefaults().getConfig().futilityPruning.getValue();

    private final int[] parentMoves = new int[MAX_PLY];

    private SearchContext searchContext;

    private boolean debug = true;

    private final MoveValidator moveValidator = new MoveValidator();

    @Getter
    private final SearchStatistics statistics = new SearchStatistics();

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

    private int negaMaximize(int ply, int depth, Color color,
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

        int mateValue = KING_WEIGHT - ply;

        statistics.nodesVisited++;
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
            if (alpha >= beta) {
                statistics.mateDistancePruningCount++;
                return alpha;
            }
        }

        boolean areWeInCheck = searchContext.isInCheck(color);
        depth = checkToExtend(areWeInCheck, color, depth);

        if (depth == 0) {
            return quiesce(ply + 1, -1, color, alpha, beta);
        }

        int hashMove = 0;

        TTResult tte = searchContext.getTTEntry();
        if (tte != null) {
            hashMove = tte.getMove();
            if (tte.getDepth() >= depth && ply != 1) {
                if (tte.isExact()) {// stored value is exact
                    statistics.ttPruningCount++;
                    return adjustScore(tte.getScore(), ply);
                } else if (tte.isLowerBound() && tte.getScore() > alpha)
                    alpha = adjustScore(tte.getScore(), ply); // update lowerbound alpha if needed
                else if (tte.isUpperBound() && tte.getScore() < beta)
                    beta = adjustScore(tte.getScore(), ply); // update upperbound beta if needed
                if (alpha >= beta) {
                    statistics.ttPruningCount++;
                    return adjustScore(tte.getScore(), ply); // if lowerbound surpasses upperbound
                }
            }
        }

        checkTimeout();

        boolean applyFutilityPruning = false;

        boolean pruneable =  not_pv && !areWeInCheck;

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
        }

        try (MoveBoardIterator moveCursor = searchContext.genSortedMovesIterator(NORMAL, ply, color, parentMove)) {

            boolean firstChild = true;

            int currSearchedNodes = statistics.nodesVisited;

            int searchedMoves = 0;

            while (moveCursor.nextMove()) {

                parentMoves[ply] = moveCursor.getMoveInt();

                /**********************************************************************
                 *  When the futility pruning flag is set, prune moves which do not    *
                 *  give  check and do not change material balance.  Some  programs    *
                 *  prune insufficient captures as well, but that seems too risky.     *
                 **********************************************************************/

                boolean quietMove=!moveCursor.isCapture() && !moveCursor.isPromotion();

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
                            && see.see_ge(searchContext.getBoard(), moveCursor, -20 * depth * depth)) {
                        statistics.seePruningCount++;
                        continue;
                    }
                }

                if (moveCursor.doValidMove()) {

                    searchedMoves++;

                    /**
                     * Late move reduction
                     */
                    int r = determineLateMoveReduction(searchedMoves, depth, moveCursor, areWeInCheck, not_pv);

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
                    searchContext.updateRootMoveScore(depth, moveCursor.getMoveInt(), score,
                            statistics.nodesVisited - currSearchedNodes);

                    if (score > max) {
                        max = score;

                        bestMove = moveCursor.getMoveInt();

                        pvArray.set(bestMove, ply);
                        searchContext.updateRootBestMove(depth, bestMove, score);

                        if (max >= beta) {
                            //                        if (!areWeInCheck) {
                            searchContext.updateCutOffHeuristics(ply, depth, color, parentMove, bestMove, moveCursor);
                            statistics.countCutOff(moveCursor, searchedMoves);
                            break;
                        }

                    }

                    // update "bad" heuristic
                    searchContext.updateBadHeuristic(depth, color, moveCursor);
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
        if (statistics.nodesVisited % 10000 == 0) {
            if (Thread.interrupted()) {
                throw new TimeoutException();
            }
        }
    }

    private int quiesce(int ply, int depth, Color color, int alpha, int beta) {
        statistics.nodesVisited++;

        if (searchContext.isDrawByMaterial()) {
            return Weights.REPETITION_WEIGHT;
        }

        if (searchContext.isRepetition()) {
            return searchContext.evaluateRepetition(color);
        }

        int hashMove = 0;

        TTResult tte = searchContext.getTTEntry();
        if (tte != null) {
            hashMove = tte.getMove();

            if (tte.getDepth() >= depth) {
                if (tte.isExact()) {// stored value is exact
                    statistics.ttPruningCount++;
                    return adjustScore(tte.getScore(), ply);
                }
                if (tte.isLowerBound() && tte.getScore() > alpha)
                    alpha = adjustScore(tte.getScore(), ply); // update lowerbound alpha if needed
                else if (tte.isUpperBound() && tte.getScore() < beta)
                    beta = adjustScore(tte.getScore(), ply); // update upperbound beta if needed
                if (alpha >= beta) {
                    statistics.ttPruningCount++;
                    return adjustScore(tte.getScore(), ply); // if lowerbound surpasses upperbound
                }
            }
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
                        statistics.deltaCutoffCount++;
                        continue;
                    }

                    if (moveCursor.doValidMove()) {
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
            int alpha, int beta, long stopTime) {

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "negamax search depth {0} [{1} - {2}]", new Object[] { depth, alpha, beta });
        }
        searchContext = new SearchContext(stc, gameState, context, depth, alpha);

        this.stopTime = stopTime;

        extensionCounter = 0;

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
     * @param board
     * @return
     */
    private static boolean noPawnPromotions(BoardRepresentation board){
        return board.getSiteToMove() == Color.WHITE && (board.getBoard().getPawns(BitChessBoard.nWhite) & BB.rank7)==0
                || board.getSiteToMove() == Color.BLACK && (board.getBoard().getPawns(BitChessBoard.nBlack) & BB.rank2)==0;
    }
}
