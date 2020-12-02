package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

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

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.createLegalMoveGenerator();

    // statistics
    private int nodesVisited = 0;
    private int nodes;
    private Move savedMove;

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
        int scoreResult = negaMaximize(currBoard, depth, color, -1000000000, +1000000000);
        Logger.info(depth, nodesVisited, scoreResult);
        Logger.log("nodes: %d, nodes searched: %d, alpha beta cutoff: %d, score: %d", nodes, nodesVisited, cutOff,
                scoreResult);
        return savedMove;
    }

    private void reset() {
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
                if (max >= beta) {
                    cutOff++;
                    break;
                }
            }

        }
        return max;
    }

}
