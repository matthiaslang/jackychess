package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.List;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveGenerator;
import org.mattlang.jc.engine.SearchMethod;

public class NegaMaxAlphaBeta implements SearchMethod {

    private EvaluateFunction evaluate;

    private MoveGenerator generator = new MoveGenerator();

    // statistics
    private int nodesVisited = 0;
    private Move savedMove;

    private int targetDepth;

    public NegaMaxAlphaBeta(EvaluateFunction evaluate) {
        this.evaluate = evaluate;
    }

    public Move search(Board currBoard, int depth, Color color) {
        assert depth > 0;
        reset();
        targetDepth = depth;
        int scoreResult = negaMaximize(currBoard, depth, color, -10000000, +10000000);
        return savedMove;
    }

    private void reset() {
        nodesVisited = 0;
        savedMove = null;
    }

    private int negaMaximize(Board currBoard, int depth, Color color,
            int alpha, int beta) {
        nodesVisited++;
        if (depth == 0)
            return evaluate.eval(currBoard, color);

        List<Move> moves = generator.generate(currBoard, color);
        int max = alpha;
        for (Move move : moves) {
            Move undoingMove = currBoard.move(move);
            int score = -negaMaximize(currBoard, depth - 1, color == WHITE ? BLACK : WHITE, -beta, -max);
            currBoard.move(undoingMove);
            if (score > max) {
                max = score;
                if (depth == targetDepth)
                    savedMove = move;
                if (max >= beta) {
                    break;
                }
            }

        }
        return max;
    }

}
