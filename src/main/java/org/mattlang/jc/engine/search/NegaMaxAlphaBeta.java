package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

public class NegaMaxAlphaBeta implements SearchMethod {

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private int maxQuiescenceDepth = Factory.getDefaults().getMaxQuiescenceDepth();

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

    public void reset() {
        nodesVisited = 0;
        quiescenceNodesVisited = 0;
        nodes = 0;
        cutOff = 0;
        savedMove = null;
        generator = Factory.getDefaults().legalMoveGenerator.create();
        evaluate = Factory.getDefaults().evaluateFunction.create();
    }

    private int negaMaximize(BoardRepresentation currBoard, int depth, Color color,
                             int alpha, int beta) {
        nodesVisited++;

        //int eval = evaluate.eval(currBoard, color);
        //if (depth == 0)
        //    return eval;

        if (depth == 0)
            return quiesce(currBoard, depth-1, color, alpha, beta);

        int eval = evaluate.eval(currBoard, color);
        // patt node:
        if (eval == -MaterialNegaMaxEval.PATT_WEIGHT || eval == MaterialNegaMaxEval.PATT_WEIGHT) {
            return eval;
        }
        if (repetitionChecker.isRepetition()) {
            // remis due to 3 times same position.
            return 0;
        }

        if (stopTime != 0 && nodesVisited % 100000 == 0) {
            if (System.currentTimeMillis() > stopTime) {
                throw new TimeoutException();
            }
            if (Thread.interrupted()) {
                throw new TimeoutException();
            }
        }

        MoveList moves = generator.generate(currBoard, color);
        if (moves.size() == 0) {
            // no more legal moves, that means we have checkmate:
            return -MaterialNegaMaxEval.KING_WHEIGHT;
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

    private int quiesce(BoardRepresentation currBoard, int depth, Color color, int alpha, int beta) {
        nodesVisited++;
        quiescenceNodesVisited++;

        int eval = evaluate.eval(currBoard, color);
        // patt node:
        if (eval == -MaterialNegaMaxEval.PATT_WEIGHT || eval == MaterialNegaMaxEval.PATT_WEIGHT) {
            return eval;
        }
        if (repetitionChecker.isRepetition()) {
            // remis due to 3 times same position.
            return 0;
        }

        if (stopTime != 0 && nodesVisited % 100000 == 0) {
            if (System.currentTimeMillis() > stopTime) {
                throw new TimeoutException();
            }
            if (Thread.interrupted()) {
                throw new TimeoutException();
            }
        }

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
            return -MaterialNegaMaxEval.KING_WHEIGHT;
        }
        nodes += moves.size();

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

    public static class NegaMaxResult {
        public final int max;
        public final List<MoveScore> moveScores;

        public NegaMaxResult(int max, List<MoveScore> moveScores) {
            this.max = max;
            this.moveScores = moveScores;
        }
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
        return new NegaMaxResult(max, moveScores);
    }

    public NegaMaxResult searchWithScore(GameState gameState, int depth,
                                         int alpha, int beta, MoveList moves, long stopTime) {
        targetDepth = depth;
        this.stopTime = stopTime;
        repetitionChecker= gameState.getRepetitionChecker();
        NegaMaxResult result = negaMaximizeWithScore(gameState.getBoard(), depth, gameState.getWho2Move(), alpha, beta, moves);

        UCILogger.info(depth, nodesVisited, result.max);
        UCILogger.log("depth:\t %d\t nodes:\t %d\t searched:\t %d\t quiescence:\t %d\t alpha beta cutoff:\t %d\t score:\t %d",
                depth, nodes, nodesVisited, quiescenceNodesVisited, cutOff, result.max);
        return result;
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
}
