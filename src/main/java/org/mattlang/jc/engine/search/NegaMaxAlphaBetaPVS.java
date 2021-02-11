package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;
import static org.mattlang.jc.engine.search.TTEntry.TTType.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.*;
import org.mattlang.jc.engine.sorting.MoveSorter;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

public class NegaMaxAlphaBetaPVS implements AlphaBetaSearchMethod, StatisticsCollector {

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private StalemateChecker stalemateChecker = Factory.getDefaults().stalemateChecker.instance();

    private MoveSorter moveSorter = Factory.getDefaults().moveSorter.instance();

    private int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    public TTCache ttCache = new TTCache();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int quiescenceNodesVisited = 0;

    private int nodes;
    private Move savedMove;
    private int savedMoveScore;

    private int targetDepth;
    private int selDepth;

    private OrderHints orderHints;

    private int cutOff;

    private boolean doPVSSearch = Factory.getDefaults().getConfig().activatePvsSearch.getValue();
    private boolean doCaching = Factory.getDefaults().getConfig().useTTCache.getValue();

    private RepetitionChecker repetitionChecker;

    public NegaMaxAlphaBetaPVS() {
        reset();
    }

    public NegaMaxAlphaBetaPVS(EvaluateFunction evaluate) {
        reset();
        this.evaluate = evaluate;
    }

    @Override
    public Move search(GameState gameState, int depth) {
        assert depth > 0;
        reset();

        MoveList moves = generateMoves(gameState.getBoard(), gameState.getWho2Move());
        NegaMaxResult rslt =
                searchWithScore(gameState, depth,
                        ALPHA_START, BETA_START, moves,
                        stopTime);
        return savedMove;
    }

    public void resetStatistics() {
        nodesVisited = 0;
        quiescenceNodesVisited = 0;
        nodes = 0;
        cutOff = 0;
        savedMove = null;
    }

    public void reset() {
        resetStatistics();
        generator = Factory.getDefaults().legalMoveGenerator.create();
        evaluate = Factory.getDefaults().evaluateFunction.create();
    }

    private int negaMaximize(BoardRepresentation currBoard, int depth, Color color,
                             int alpha, int beta, PVList pvList) {
        nodesVisited++;

        PVList myPvlist = new PVList();

        if (doCaching) {
            TTEntry tte = ttCache.getTTEntry(currBoard, color);
            if (tte != null && tte.depth >= depth) {
                if (tte.type == EXACT_VALUE) // stored value is exact
                    return tte.value;
                if (tte.type == LOWERBOUND && tte.value > alpha)
                    alpha = tte.value; // update lowerbound alpha if needed
                else if (tte.type == UPPERBOUND && tte.value < beta)
                    beta = tte.value; // update upperbound beta if needed
                if (alpha >= beta)
                    return tte.value; // if lowerbound surpasses upperbound
            }
        }

        if (depth == 0) {
            pvList.clear();
            return quiesce(currBoard, - 1, color, alpha, beta);
        }

        int pattCheckEval = stalemateChecker.eval(currBoard, color);
        // patt node:
        if (pattCheckEval == -PATT_WEIGHT || pattCheckEval == PATT_WEIGHT) {
            return pattCheckEval;
        }
        //        if (repetitionChecker.isRepetition()) {
        //            // remis due to 3 times same position.
        //            return -REPETITION_WEIGHT;
        //        }

        checkTimeout();

        MoveList moves = generator.generate(currBoard, color);
        if (moves.size() == 0) {
            // no more legal moves, that means we have checkmate:
            return -KING_WEIGHT;
        }
        moves = moveSorter.sort(moves, orderHints, depth, targetDepth);

        nodes += moves.size();
        int max = alpha;
        boolean firstChild = true;
        for (MoveCursor moveCursor : moves) {
            // we do not evaluate repetitions, we always want to either win or loos:
            if (!repetitionChecker.isRepetition()) {
                moveCursor.move(currBoard);
                repetitionChecker.push(currBoard);

                int score;
                if (firstChild) {
                    score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -max, myPvlist);
                    if (doPVSSearch) {
                        firstChild = false;
                    }
                } else {
                    score = -negaMaximize(currBoard, depth - 1, color.invert(), -max - 1, -max, myPvlist);
                    if (max < score && score < beta) {
                        score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -score, myPvlist);
                    }
                }

                moveCursor.undoMove(currBoard);
                repetitionChecker.pop();
                if (score > max) {
                    max = score;

                    pvList.set(moveCursor.getMove());
                    pvList.add(myPvlist);
                    if (depth == targetDepth) {
                        savedMove = moveCursor.getMove();
                        savedMoveScore = score;
                    }
                    if (max >= beta) {
                        cutOff++;
                        break;
                    }
                }
            }
        }
        if (doCaching) {
            ttCache.storeTTEntry(currBoard, color, max, max, beta, depth);
        }
        return max;
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

        int eval = evaluate.eval(currBoard, color);
        // patt node:
        if (eval == -PATT_WEIGHT || eval == PATT_WEIGHT) {
            return eval;
        }
        //        if (repetitionChecker.isRepetition()) {
        //            // remis due to 3 times same position.
        //            return -REPETITION_WEIGHT;
        //        }

        /* are we too deep? */
        if (depth < -maxQuiescenceDepth) {
            ttCache.storeTTEntry(currBoard, color, eval, alpha, beta, depth);
            return eval;
        }

        checkTimeout();

        /* check with the evaluation function */
        int x = eval;
        if (x >= beta)
            return beta;
        if (x > alpha)
            alpha = x;

        MoveList moves = generator.generate(currBoard, color);
        if (moves.size() == 0) {
            // no more legal moves, that means we have checkmate:
            return -KING_WEIGHT;
        }
        nodes += moves.size();
        quiescenceNodesVisited++;
        // depth is negative inside quiescence; now update selDepth to the maximum of quiescence depth we have
        // ever used in this round:
        if (targetDepth - depth > selDepth) {
            selDepth = targetDepth - depth;
        }

        /* loop through the capture moves */
        for (MoveCursor moveCursor : moves) {
            if (moveCursor.isCapture() && !repetitionChecker.isRepetition()) {
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
        ttCache.storeTTEntry(currBoard, color, x, alpha, beta, depth);

        return alpha;
    }

    private NegaMaxResult negaMaximizeWithScore(BoardRepresentation currBoard, int depth, Color color,
                                                int alpha, int beta, MoveList moves) {
        nodesVisited++;
        ArrayList<MoveScore> moveScores = new ArrayList<>();


        nodes += moves.size();
        int max = alpha;
        boolean firstChild = true;

        moves = moveSorter.sort(moves, orderHints, depth, targetDepth);
        PVList pvList = new PVList();

        PVList myPvlist = new PVList();

        for (MoveCursor moveCursor : moves) {
            // we do not evaluate repetitions, we always want to either win or loos:
            if (!repetitionChecker.isRepetition()) {
                moveCursor.move(currBoard);
                repetitionChecker.push(currBoard);

                int score;
                if (firstChild) {
                    score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -max, myPvlist);
                    if (doPVSSearch) {
                        firstChild = false;
                    }
                } else {
                    score = -negaMaximize(currBoard, depth - 1, color.invert(), -max - 1, -max, myPvlist);
                    if (max < score && score < beta) {
                        score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -score, myPvlist);
                    }
                }

                moveScores.add(new MoveScore(moveCursor.getMove(), score));
                moveCursor.undoMove(currBoard);
                repetitionChecker.pop();
                if (score > max) {
                    max = score;
                    pvList.set(moveCursor.getMove());
                    pvList.add(myPvlist);
                    if (depth == targetDepth) {
                        savedMove = moveCursor.getMove();
                        savedMoveScore = score;
                    }
                    if (max >= beta) {
                        cutOff++;
                        break;
                    }
                }
            }
        }
        return new NegaMaxResult(max, moveScores, pvList, targetDepth, selDepth);
    }

    public NegaMaxResult searchWithScore(GameState gameState, int depth,
            int alpha, int beta, MoveList moves, long stopTime, OrderHints orderHints) {
        targetDepth = depth;
        selDepth = depth;
        this.orderHints = orderHints;
        this.stopTime = stopTime;
        repetitionChecker = gameState.getRepetitionChecker();
        NegaMaxResult result =
                negaMaximizeWithScore(gameState.getBoard(), depth, gameState.getWho2Move(), alpha, beta, moves);

        //UCILogger.info(depth, nodesVisited, result.max);
        // UCILogger.log(" %d\t %d/%d\t %d\t %d\t %d",
        //         depth, nodes, nodesVisited, quiescenceNodesVisited, cutOff, result.max);
        return result;
    }

    public Move getSavedMove() {
        return savedMove;
    }

    public MoveList generateMoves(BoardRepresentation currBoard, Color color) {
        return generator.generate(currBoard, color);
    }

    @Override
    public NegaMaxResult searchWithScore(GameState gameState, int depth,
            int alpha, int beta, MoveList moves, long stopTime) {
        return searchWithScore(gameState, depth, alpha, beta, moves, stopTime, OrderHints.NO_HINTS);
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

    public int getSavedMoveScore() {
        return savedMoveScore;
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
        ttCache = new TTCache();
    }
}