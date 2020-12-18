package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
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

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int nodes;
    private Move savedMove;
    private int savedMoveScore;

    private int targetDepth;
    private int cutOff;


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
        targetDepth = depth;
        int scoreResult = negaMaximize(gameState.getBoard(), depth, gameState.getWho2Move(), ALPHA_START, BETA_START);
        UCILogger.info(depth, nodesVisited, scoreResult);
        UCILogger.log("nodes: %d, nodes searched: %d, alpha beta cutoff: %d, score: %d", nodes, nodesVisited, cutOff,
                scoreResult);
        return savedMove;
    }

    public void reset() {
        nodesVisited = 0;
        nodes = 0;
        cutOff = 0;
        savedMove = null;
        generator = Factory.getDefaults().legalMoveGenerator.create();
        evaluate = Factory.getDefaults().evaluateFunction.create();
    }

    private int negaMaximize(BoardRepresentation currBoard, int depth, Color color,
                             int alpha, int beta) {
        nodesVisited++;
        if (depth == 0)
            return evaluate.eval(currBoard, color);

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
            int score = -negaMaximize(currBoard, depth - 1, color == WHITE ? BLACK : WHITE, -beta, -max);
            moveCursor.undoMove(currBoard);
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
            int score = -negaMaximize(currBoard, depth - 1, color == WHITE ? BLACK : WHITE, -beta, -max);
            moveScores.add(new MoveScore(moveCursor.getMove(), score));
            moveCursor.undoMove(currBoard);
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

    public NegaMaxResult searchWithScore(BoardRepresentation currBoard, int depth, Color color,
                                         int alpha, int beta, MoveList moves, long stopTime) {
        targetDepth = depth;
        this.stopTime = stopTime;
        NegaMaxResult result = negaMaximizeWithScore(currBoard, depth, color, alpha, beta, moves);
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
