package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.*;
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

    private int extensionCounter = 0;

    private OrderCalculator orderCalculator;

    private int cutOff;

    private boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();

    private boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();
    private boolean staticNullMove = Factory.getDefaults().getConfig().staticNullMove.getValue();
    private boolean razoring = Factory.getDefaults().getConfig().razoring.getValue();
    private boolean deltaCutOff = Factory.getDefaults().getConfig().deltaCutoff.getValue();
    private boolean useLateMoveReductions = Factory.getDefaults().getConfig().useLateMoveReductions.getValue();

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
        boolean not_pv = Math.abs(beta - alpha) <= 1;

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

         /*
        long tte = searchContext.getTTEntry(color);
        if (tte != 0 && ply != 1 && TTCache3.getDepth(tte) >= depth) {
            int tteScore = TTCache3.getScore(tte);
            int tteType = TTCache3.getFlag(tte);
            if (tteType == TTEntry.EXACT_VALUE) // stored value is exact
                return adjustScore(tteScore, ply);
            if (tteType == TTEntry.LOWERBOUND && tteScore > alpha)
                alpha = adjustScore(tteScore, ply); // update lowerbound alpha if needed
            else if (tteType == TTEntry.UPPERBOUND && tteScore < beta)
                beta = adjustScore(tteScore, ply); // update upperbound beta if needed
            if (alpha >= beta)
                return adjustScore(tteScore, ply); // if lowerbound surpasses upperbound
        }
        */
        if (depth == 0) {
            return quiesce(ply + 1, -1, color, alpha, beta);
        }
        checkTimeout();

        /**************************************************************************
         * EVAL PRUNING / STATIC NULL MOVE                                         *
         **************************************************************************/

        if (staticNullMove
                && depth < 3
                && not_pv
                && !areWeInCheck
                && Math.abs(beta - 1) > ALPHA_START + 100) {
            int static_eval = searchContext.eval(color);

            int eval_margin = 120 * depth;
            if (static_eval - eval_margin >= beta)
                return static_eval - eval_margin;
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
                not_pv &&
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

        /**************************************************************************
         *  RAZORING - if a node is close to the leaf and its static score is low, *
         *  we drop directly to the quiescence search.                             *
         **************************************************************************/

        if (razoring
                && not_pv
                && !areWeInCheck
                && tte == null
                && searchContext.getNullMoveCounter() == 0
                //	&&  !(bbPc(p, p->side, P) & bbRelRank[p->side][RANK_7]) // no pawns to promote in one move
                && depth <= 3) {
            int threshold = alpha - 300 - (depth - 1) * 60;
            if (searchContext.eval(color) < threshold) {
                int val = quiesce(ply + 1, -1, color, alpha, beta);
                if (val < threshold)
                    return alpha;
            }
        } // end of razoring code

        int max = alpha;

        int bestMove = 0;

        pvArray.reset(ply);

        try (MoveList moves = searchContext.generateMoves(color)) {

            sortMoves(ply, depth, color, moves, searchContext.getBoard());

            nodes += moves.size();

            boolean firstChild = true;

            int searchedMoves = 0;
            for (MoveCursor moveCursor : moves) {
                searchContext.doMove(moveCursor);

                // skip illegal moves:
                if (searchContext.isInCheck(color)) {
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

        boolean areWeInCheck = searchContext.isInCheck(color);

        Color opponent=color.invert();

        try (MoveList moves = searchContext.generateMoves(color)) {
            nodes += moves.size();
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
                            && x + pieceValue[moveCursor.getCapturedFigure()] + 200 < alpha
                            && searchContext.isOpeningOrMiddleGame()
                    ) {
                        searchContext.undoMove(moveCursor);
                        continue;
                    }

                    /*
                    if ( ( stand_pat + e.PIECE_VALUE[ movelist[i].piece_cap ] + 200 < alpha )
                            &&   ( b.piece_material[!b.stm] - e.PIECE_VALUE[movelist[i].piece_cap] > e.ENDGAME_MAT )
                            &&   ( !move_isprom(movelist[i]) ) )
                        continue;
                    */
                    /**********************************************************************
                     *  badCapture() replaces a cutoff based on the Static Exchange Evalu- *
                     *  ation, marking the place where it ought to be coded. Despite being *
                     *  just a hack, it saves quite a few nodes.                           *
                     **********************************************************************/

                    /*
                    if ( badCapture( movelist[i] )
                            &&  !move_canSimplify( movelist[i] )
                            &&  !move_isprom( movelist[i] ) )
                        continue;
                      */

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

    private static int[] pieceValue = new int[128];
    static {
        pieceValue[FigureType.Pawn.figureCode] = 100;
        pieceValue[Figure.B_Pawn.figureCode] = 100;
        pieceValue[Figure.W_Pawn.figureCode] = 100;

        pieceValue[FigureType.Knight.figureCode] = 300;
        pieceValue[Figure.B_Knight.figureCode] = 300;
        pieceValue[Figure.W_Knight.figureCode] = 300;

        pieceValue[FigureType.Bishop.figureCode] = 330;
        pieceValue[Figure.B_Bishop.figureCode] = 330;
        pieceValue[Figure.W_Bishop.figureCode] = 330;

        pieceValue[FigureType.Rook.figureCode] = 500;
        pieceValue[Figure.B_Rook.figureCode] = 500;
        pieceValue[Figure.W_Rook.figureCode] = 500;

        pieceValue[FigureType.Queen.figureCode] = 900;
        pieceValue[Figure.B_Queen.figureCode] = 900;
        pieceValue[Figure.W_Queen.figureCode] = 900;

        pieceValue[FigureType.King.figureCode] = 32000;
        pieceValue[Figure.B_King.figureCode] = 32000;
        pieceValue[Figure.W_King.figureCode] = 32000;

    }

    //
    //    e.ENDGAME_MAT = 1300;

        boolean badCapture(MoveCursor move) {

            /* captures by pawn do not lose material */
            if (move.getFigureType() == FigureConstants.FT_PAWN ) return false;

            /* Captures "lower takes higher" (as well as BxN) are good by definition. */
            if ( pieceValue[move.getCapturedFigure()] >= pieceValue[move.getFigureType()] - 50 )
                return false;

            /**************************************************************************
             *   When the enemy piece is defended by a pawn, in the quiescence search  *
             *   we  will  accept rook takes minor, but not minor takes pawn. ( More   *
             *   exact  version  should accept B/N x P if (a) the pawn  is  the  sole  *
             *   defender and (b) there is more than one attacker.                     *
             **************************************************************************/
//
//            if (b.pawn_ctrl[b.color[move.from] ^ 1][move.to]
//                    && e.PIECE_VALUE[move.piece_cap] + 200 < e.PIECE_VALUE[move.piece_from])
//                return 1;
//
//            if (pieceValue[move.getCapturedFigure()] + 500 < pieceValue[move.getFigureType()]) {
//                if (leaperAttack(b.color[move.from] ^ 1, move.to, KNIGHT)) return 1;
//                if (bishAttack(b.color[move.from] ^ 1, move.to, NE)) return 1;
//                if (bishAttack(b.color[move.from] ^ 1, move.to, NW)) return 1;
//                if (bishAttack(b.color[move.from] ^ 1, move.to, SE)) return 1;
//                if (bishAttack(b.color[move.from] ^ 1, move.to, SW)) return 1;
//            }

            /* if a capture is not processed, it cannot be considered bad */
            return false;
        }


//
//        int move_canSimplify(smove m) {
//            if ( m.piece_cap == PAWN
//                    ||   b.piece_material[!b.stm] - e.PIECE_VALUE[m.piece_cap] > e.ENDGAME_MAT )
//                return 0;
//            else
//                return 1;
//        }

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

        return new NegaMaxResult(directScore, pvArray.getPvMoves(),  searchContext, nodesVisited, quiescenceNodesVisited);

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
