package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.engine.evaluation.Weights.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.*;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

public class NegaMaxAlphaBeta implements AlphaBetaSearchMethod, StatisticsCollector {

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private StalemateChecker stalemateChecker = Factory.getDefaults().stalemateChecker.instance();

    private int maxQuiescenceDepth = Factory.getDefaults().getConfig().maxQuiescence.getValue();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int quiescenceNodesVisited = 0;

    private int nodes;
    private Move savedMove;
    private int savedMoveScore;

    private int targetDepth;
    private int cutOff;

    private RepetitionChecker repetitionChecker;


    public NegaMaxAlphaBeta() {
        reset();
    }

    public NegaMaxAlphaBeta(EvaluateFunction evaluate) {
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
                             int alpha, int beta) {
        nodesVisited++;

        if (depth == 0)
            return quiesce(currBoard, depth-1, color, alpha, beta);

        int pattCheckEval = stalemateChecker.eval(currBoard, color);
        // patt node:
        if (pattCheckEval == -PATT_WEIGHT || pattCheckEval == PATT_WEIGHT) {
            return pattCheckEval;
        }
        if (repetitionChecker.isRepetition()) {
            // remis due to 3 times same position.
            return -REPETITION_WEIGHT;
        }

        checkTimeout();

        MoveList moves = generator.generate(currBoard, color);
        if (moves.size() == 0) {
            // no more legal moves, that means we have checkmate:
            return -KING_WEIGHT;
        }
        nodes += moves.size();
        int max = alpha;
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);
            repetitionChecker.push(currBoard);
            int score = -negaMaximize(currBoard, depth - 1, color.invert(), -beta, -max);
            moveCursor.undoMove(currBoard);
            repetitionChecker.pop();
            if (score > max) {
                max = score;
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
        if (repetitionChecker.isRepetition()) {
            // remis due to 3 times same position.
            return -REPETITION_WEIGHT;
        }

        checkTimeout();

        /* are we too deep? */
        if (depth< -maxQuiescenceDepth)
            return  eval;


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

        /* loop through the capture moves */
        for (MoveCursor moveCursor : moves) {
            if (moveCursor.isCapture()) {
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
        return alpha;
    }

    private NegaMaxResult negaMaximizeWithScore(BoardRepresentation currBoard, int depth, Color color,
                                                int alpha, int beta, MoveList moves) {
        nodesVisited++;
        ArrayList<MoveScore> moveScores = new ArrayList<>();


        nodes += moves.size();
        int max = alpha;
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);
            repetitionChecker.push(currBoard);
            int score = -negaMaximize(currBoard, depth - 1, color == WHITE ? BLACK : WHITE, -beta, -max);
            moveScores.add(new MoveScore(moveCursor.getMove(), score));
            moveCursor.undoMove(currBoard);
            repetitionChecker.pop();
            if (score > max) {
                max = score;
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
        return new NegaMaxResult(max, moveScores, new PVList());
    }

    public NegaMaxResult searchWithScore(GameState gameState, int depth,
                                         int alpha, int beta, MoveList moves, long stopTime) {
        targetDepth = depth;
        this.stopTime = stopTime;
        repetitionChecker= gameState.getRepetitionChecker();
        NegaMaxResult result = negaMaximizeWithScore(gameState.getBoard(), depth, gameState.getWho2Move(), alpha, beta, moves);

        UCILogger.info(depth, nodesVisited, result.max);
        UCILogger.log(" %d\t %d/%d\t %d\t %d\t %d",
                depth, nodes, nodesVisited, quiescenceNodesVisited, cutOff, result.max);
        return result;
    }


    public NegaMaxResult searchWithScore(GameState gameState, int depth,
            int alpha, int beta, MoveList moves, long stopTime, PVList prevPvList) {
       // todo support pvlist
       return searchWithScore(gameState, depth, alpha, beta, moves, stopTime);
    }


    public Move getSavedMove() {
        return savedMove;
    }

    public MoveList generateMoves(BoardRepresentation currBoard, Color color) {
        return generator.generate(currBoard, color);
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
}
