package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.Factory;
import org.mattlang.jc.Logger;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

public class NegaMaxAlphaBeta implements SearchMethod {

    public static final int ALPHA_START = -1000000000;
    public static final int BETA_START = +1000000000;

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.createLegalMoveGenerator();

    private long stopTime = 0;

    // statistics
    private int nodesVisited = 0;
    private int nodes;
    private Move savedMove;
    private int savedMoveScore;

    private int targetDepth;
    private int cutOff;


    public NegaMaxAlphaBeta() {
        this.evaluate = Factory.createEvaluateFunction();
    }

    public NegaMaxAlphaBeta(EvaluateFunction evaluate) {
        this.evaluate = evaluate;
    }

    public Move search(Board currBoard, int depth, Color color) {
        assert depth > 0;
        reset();
        targetDepth = depth;
        int scoreResult = negaMaximize(currBoard, depth, color, ALPHA_START, BETA_START);
        Logger.info(depth, nodesVisited, scoreResult);
        Logger.log("nodes: %d, nodes searched: %d, alpha beta cutoff: %d, score: %d", nodes, nodesVisited, cutOff,
                scoreResult);
        return savedMove;
    }

    public void reset() {
        nodesVisited = 0;
        nodes = 0;
        cutOff = 0;
        savedMove = null;
        generator = Factory.createLegalMoveGenerator();
    }

    private int negaMaximize(Board currBoard, int depth, Color color,
            int alpha, int beta) {
        nodesVisited++;
        if (depth == 0)
            return evaluate.eval(currBoard, color);

        if (stopTime != 0 && nodesVisited % 100000 == 0) {
            if (System.currentTimeMillis() > stopTime) {
                throw new TimeoutException();
            }
        }

        MoveList moves = generator.generate(currBoard, color);
        nodes += moves.size();
        int max = alpha;
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);
            int score = -negaMaximize(currBoard, depth - 1, color == WHITE ? BLACK : WHITE, -beta, -max);
            moveCursor.undoMove(currBoard);
            if (score > max) {
                max = score;
                if (depth == targetDepth)
                    savedMove = moveCursor.getMove();
                    savedMoveScore = score;
                if (max >= beta) {
                    cutOff++;
                    break;
                }
            }

        }
        return max;
    }

    public static class MoveScore{

        public final Move move;
        public final int score;

        public MoveScore(Move move, int score) {
            this.move = move;
            this.score = score;
        }
    }

    private List<MoveScore> negaMaximizeWithScore(Board currBoard, int depth, Color color,
            int alpha, int beta, MoveList moves) {
        nodesVisited++;
        ArrayList<MoveScore> result = new ArrayList<>();


        nodes += moves.size();
        int max = alpha;
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);
            int score = -negaMaximize(currBoard, depth - 1, color == WHITE ? BLACK : WHITE, -beta, -max);
            result.add(new MoveScore(moveCursor.getMove(), score));
            moveCursor.undoMove(currBoard);
            if (score > max) {
                max = score;
                if (depth == targetDepth)
                    savedMove = moveCursor.getMove();
                if (max >= beta) {
                    cutOff++;
                    break;
                }
            }

        }
        return result;
    }

    public List<MoveScore> searchWithScore(Board currBoard, int depth, Color color,
            int alpha, int beta, MoveList moves, long stopTime) {
        targetDepth = depth;
        this.stopTime = stopTime;
        List<MoveScore> scoreResult = negaMaximizeWithScore(currBoard, depth, color, alpha, beta, moves);
        return scoreResult;
    }

    public Move getSavedMove() {
        return savedMove;
    }

    public MoveList generateMoves(Board currBoard, Color color) {
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
