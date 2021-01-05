package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.movegenerator.BoardCache;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

import java.util.ArrayList;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.engine.search.TTEntry.TTType.*;

public class NegaMaxAlphaBetaTT implements AlphaBetaSearchMethod {

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


    public NegaMaxAlphaBetaTT() {
        reset();
    }

    public NegaMaxAlphaBetaTT(EvaluateFunction evaluate) {
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

        TTEntry tte = getTTEntry(currBoard, color);
        if(tte != null && tte.depth >= depth)
        {
            if(tte.type == EXACT_VALUE) // stored value is exact
                return tte.value;
            if(tte.type == LOWERBOUND && tte.value > alpha)
                alpha = tte.value; // update lowerbound alpha if needed
            else if(tte.type == UPPERBOUND && tte.value < beta)
                beta = tte.value; // update upperbound beta if needed
            if(alpha >= beta)
                return tte.value; // if lowerbound surpasses upperbound
        }

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
        if(max <= alpha) // a lowerbound value
            storeTTEntry(currBoard, color, max, LOWERBOUND, depth);
        else if(max >= beta) // an upperbound value
            storeTTEntry(currBoard, color, max, UPPERBOUND, depth);
        else // a true minimax value
            storeTTEntry(currBoard, color, max, EXACT_VALUE, depth);
        //return best;


        return max;
    }

    private BoardCache<TTEntry> ttCache = new BoardCache<>((board, side) -> null);

    private TTEntry getTTEntry(BoardRepresentation currBoard, Color side) {
        return ttCache.get(currBoard, side);
    }


    private void storeTTEntry(BoardRepresentation board, Color side, int eval, TTEntry.TTType tpe, int depth) {
       // only store entries with lower depth:
        TTEntry existing = ttCache.get(board, side);
        if (existing == null || existing.depth> depth) {
            ttCache.put(board, side, new TTEntry(eval, tpe, depth));
        }
    }

    private int quiesce(BoardRepresentation currBoard, int depth, Color color, int alpha, int beta) {
        nodesVisited++;


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
        if (depth< -maxQuiescenceDepth) {
            if(eval <= alpha) // a lowerbound value
                storeTTEntry(currBoard, color, eval, LOWERBOUND, depth);
            else if(eval >= beta) // an upperbound value
                storeTTEntry(currBoard, color, eval, UPPERBOUND, depth);
            else // a true minimax value
                storeTTEntry(currBoard, color, eval, EXACT_VALUE, depth);

            return eval;
        }


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
        if(x <= alpha) // a lowerbound value
            storeTTEntry(currBoard, color, x, LOWERBOUND, depth);
        else if(x >= beta) // an upperbound value
            storeTTEntry(currBoard, color, x, UPPERBOUND, depth);
        else // a true minimax value
            storeTTEntry(currBoard, color, x, EXACT_VALUE, depth);
        //return best;

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
