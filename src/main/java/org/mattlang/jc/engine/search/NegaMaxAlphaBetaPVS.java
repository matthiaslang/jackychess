package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.evaluation.PhaseCalculator.isOpeningOrMiddleGame;
import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.*;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTEntry;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.uci.GameContext;

/**
 * Negamax with Alpha Beta Pruning. Supports PVS Search which could be optional activated.
 * Supports TT Cache (not working properly currently)
 */
public class NegaMaxAlphaBetaPVS implements AlphaBetaSearchMethod, StatisticsCollector {

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();

    private int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    private boolean useHistoryHeuristic = Factory.getDefaults().getConfig().useHistoryHeuristic.getValue();
    private boolean useKillerMoves = Factory.getDefaults().getConfig().useKillerMoves.getValue();

    public TTCache ttCache;

    private PVTriangularArray pvArray = new PVTriangularArray();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int quiescenceNodesVisited = 0;

    private int nodes;
    private Move savedMove;
    private int savedMoveScore;

    private int targetDepth;
    private int selDepth;

    private int nullMoveCounter;
    private int enPassantBeforeNullMove;
    private RepetitionChecker repetitionCheckerBeforeNullMove;
    private boolean isOpeningOrMiddleGame;

    private OrderHints orderHints;

    private OrderCalculator orderCalculator;

    private int cutOff;

    private boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();
    private boolean doCaching = Factory.getDefaults().getConfig().useTTCache.getValue();
    private boolean useNullMoves = Factory.getDefaults().getConfig().useNullMoves.getValue();

    private boolean doExtend = false;

    private RepetitionChecker repetitionChecker;

    private ArrayList<MoveScore> moveScores ;

    private HistoryHeuristic historyHeuristic = null;

    private KillerMoves killerMoves = null;

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
        return savedMove;
    }

    private void initContext(GameContext context) {
          killerMoves = context.getKillerMoves();
          historyHeuristic = context.getHistoryHeuristic();
          ttCache = context.getTtCache();
    }

    public void resetStatistics() {
        nodesVisited = 0;
        quiescenceNodesVisited = 0;
        nodes = 0;
        cutOff = 0;
        savedMove = null;
        savedMoveScore = 0;
        nullMoveCounter = 0;
    }

    public void reset() {
        resetStatistics();
        generator = Factory.getDefaults().legalMoveGenerator.create();
        evaluate = Factory.getDefaults().evaluateFunction.create();
    }

    private int negaMaximize(BoardRepresentation currBoard, int depth, Color color,
            int alpha, int beta) {
        nodesVisited++;

        int ply = targetDepth - depth + 1;

        if (repetitionChecker.isRepetition()) {
            return Weights.REPETITION_WEIGHT;
        }

        if (doCaching) {
            TTEntry tte = ttCache.getTTEntry(currBoard, color);
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
        }

        if (depth == 0) {
            return quiesce(currBoard, -1, color, alpha, beta);
        }

        boolean areWeInCheck = checkChecker.isInChess(currBoard, color);

        checkTimeout();

        /**
         * null move reduction:
         * only, for non pv nodes,
         * and no null move has already been chosen in this row
         * and we are not in end game (because of zugzwang issues)
         * and we are not in check (also for zugzwang)
         */
        if (useNullMoves &&
                beta - alpha <= 1 &&
                nullMoveCounter == 0 &&
                !areWeInCheck &&
                depth > 1 &&
                isOpeningOrMiddleGame
        ) {
            int R = /*(depth > 6) ? 3 :*/ 2;
            if (R > depth) {
                R = depth;
            }

            doPrepareNullMove(currBoard);
            int eval = -negaMaximize(currBoard, depth - R, color.invert(), -beta, -beta + 1);
            undoNullMove(currBoard);

            if (eval >= beta) {
                return eval;
            }
        }

        int max = alpha;

        try (MoveList moves = generator.generate(currBoard, color)) {
            if (moves.isCheckMate()) {
                if (areWeInCheck) {
                    // no more legal moves, that means we have checkmate:
                    // in negamax "our" score is highest, so we need to return the negative king weight adjusted by ply:
                    return -KING_WEIGHT + ply;
                } else {
                    return -PATT_WEIGHT;
                }
            }

            orderCalculator.prepareOrder(color, depth);
            moves.sort(orderCalculator);

            nodes += moves.size();

            boolean firstChild = true;

            depth = checkToExtend(currBoard, color, depth);

            for (MoveCursor moveCursor : moves) {
                moveCursor.move(currBoard);
                repetitionChecker.push(currBoard);

                int score;
                if (firstChild) {
                    score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -max);
                    if (doPVSSearch) {
                        firstChild = false;
                    }
                } else {
                    score = -negaMaximize(currBoard, depth - 1, color.invert(), -max - 1, -max);
                    if (max < score && score < beta) {
                        score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -score);
                    }
                }

                if (depth == targetDepth) {
                    moveScores.add(new MoveScore(moveCursor.getMove(), score));
                }

                moveCursor.undoMove(currBoard);
                repetitionChecker.pop();
                if (score > max) {
                    max = score;

                    Move currMove = moveCursor.getMove();
                    pvArray.set(currMove, ply);
                    if (depth == targetDepth) {
                        savedMove = currMove;
                        savedMoveScore = score;
                    }
                    if (max >= beta) {
                        if (useHistoryHeuristic && !moveCursor.isCapture()) {
                            updateHistoryHeuristic(color, currMove, depth);
                        }
                        if (useKillerMoves && !moveCursor.isCapture()) {
                            updateKillerMoves(color, currMove, targetDepth - depth);
                        }
                        cutOff++;
                        break;
                    }

                }
            }
        }

        storeTT(currBoard, color, max, max, beta, depth);

        return max;
    }

    private void undoNullMove(BoardRepresentation currBoard) {
        nullMoveCounter--;
        currBoard.setEnPassantOption(enPassantBeforeNullMove);
        repetitionChecker = repetitionCheckerBeforeNullMove;
    }

    private void doPrepareNullMove(BoardRepresentation currBoard) {
        nullMoveCounter++;
        enPassantBeforeNullMove = currBoard.getEnPassantMoveTargetPos();
        currBoard.setEnPassantOption(Board3.NO_EN_PASSANT_OPTION);

        repetitionCheckerBeforeNullMove = repetitionChecker;
        repetitionChecker = new SimpleRepetitionChecker();
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

    private void updateHistoryHeuristic(Color color, Move move, int depth) {
        historyHeuristic.update(color, move, depth);
    }

    private void updateKillerMoves(Color color, Move move, int ply) {
        killerMoves.addKiller(color, move, ply);
    }

    private void storeTT(BoardRepresentation currBoard, Color color, int max, int alpha, int beta, int depth) {
        if (doCaching) {
            ttCache.storeTTEntry(currBoard, color, max, alpha, beta, depth);
        }
    }

    private void checkTimeout() {
        if (stopTime != 0 && nodesVisited % 100000 == 0) {
            if (System.currentTimeMillis() > stopTime) {
                throw new TimeoutException();
            }
            if (Thread.interrupted()) {
                throw new TimeoutException();
            }
        }
    }

    private int quiesce(BoardRepresentation currBoard, int depth, Color color, int alpha, int beta) {
        nodesVisited++;

        int ply = targetDepth - depth +1;

        if (repetitionChecker.isRepetition()) {
            return Weights.REPETITION_WEIGHT;
        }

        boolean areWeInCheck= checkChecker.isInChess(currBoard, color);

        int eval = evaluate.eval(currBoard, color);

        /* are we too deep? */
        if (depth < -maxQuiescenceDepth) {
            storeTT(currBoard, color, eval, alpha, beta, depth);
            return eval;
        }

        checkTimeout();

        /* check with the evaluation function */
        int x = eval;
        if (x >= beta)
            return beta;
        if (x > alpha)
            alpha = x;

        try (MoveList moves = generator.generateNonQuietMoves(currBoard, color)) {
            if (moves.isCheckMate()) {
                if (areWeInCheck) {
                    // no more legal moves, that means we have checkmate:
                    return -KING_WEIGHT + ply;
                } else {
                    return -PATT_WEIGHT;
                }
            }

            nodes += moves.size();
            quiescenceNodesVisited++;
            // depth is negative inside quiescence; now update selDepth to the maximum of quiescence depth we have
            // ever used in this round:
            if (targetDepth - depth > selDepth) {
                selDepth = targetDepth - depth;
            }

            // sort just by MMV-LVA, as we have no pv infos currently in quiescence...
            orderCalculator.prepareOrder(color, depth);
            moves.sort(orderCalculator);

            /* loop through the capture moves */
            for (MoveCursor moveCursor : moves) {
                moveCursor.move(currBoard);
                repetitionChecker.push(currBoard);
                x = -quiesce(currBoard, depth - 1, color.invert(), -beta, -alpha);
                moveCursor.undoMove(currBoard);
                repetitionChecker.pop();
                if (x > alpha) {
                    if (x >= beta)
                        return beta;
                    alpha = x;
                }
            }
        }

        storeTT(currBoard, color, x, alpha, beta, depth);

        return alpha;
    }

    private int checkToExtend(BoardRepresentation currBoard, Color color, int currDepth) {
        if (doExtend) {
            if (checkChecker.isInChess(currBoard, color)) {
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
        targetDepth = depth;
        selDepth = depth;
        this.orderHints = orderHints;
        this.orderCalculator = new OrderCalculator(orderHints, targetDepth);
        this.stopTime = stopTime;
        repetitionChecker = gameState.getRepetitionChecker();
        moveScores = new ArrayList<>();
        savedMoveScore = alpha;
        savedMove = null;
        isOpeningOrMiddleGame= isOpeningOrMiddleGame(gameState.getBoard());

        pvArray.reset();

        initContext(context);

        int directScore = negaMaximize(gameState.getBoard(), depth, gameState.getWho2Move(), alpha, beta);

        return new NegaMaxResult(directScore, savedMoveScore, savedMove, moveScores, pvArray.getPvMoves(), targetDepth,
                selDepth);

    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    public int getNodes() {
        return nodes;
    }

    public int getCutOff() {
        return cutOff;
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
            if (maxQuiescenceDepth>0) {
                rslts.put("quiescenceNodesVisited", quiescenceNodesVisited);
            }
            rslts.put("cutoff", cutOff);
            rslts.put("savedMove", savedMove);
            rslts.put("savedMoveScore", savedMoveScore);
            Map ttcacheMap = new LinkedHashMap();
            ttCache.collectStatistics(ttcacheMap);
            rslts.put("ttcache", ttcacheMap);
        }
    }

    public boolean isDoPVSSearch() {
        return doPVSSearch;
    }

    public NegaMaxAlphaBetaPVS setDoPVSSearch(boolean doPVSSearch) {
        this.doPVSSearch = doPVSSearch;
        return this;
    }

    public boolean isDoCaching() {
        return doCaching;
    }

    public NegaMaxAlphaBetaPVS setDoCaching(boolean doCaching) {
        this.doCaching = doCaching;
        return this;
    }

    @Override
    public void resetCaches() {
        //ttCache = new TTCache();
    }
}
