package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.List;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.LegalMoveGenerator;
import org.mattlang.jc.engine.MoveGenerator;
import org.mattlang.jc.engine.SearchMethod;

public class NegaMax implements SearchMethod {

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = new LegalMoveGenerator();

    // statistics
    private int nodesVisited = 0;

    static class ScoreResult {

        public final Move bestMove;
        public final int score;

        public ScoreResult(Move bestMove, int score) {
            this.bestMove = bestMove;
            this.score = score;
        }
    }

    public NegaMax(EvaluateFunction evaluate) {
        this.evaluate = evaluate;
    }

    public Move search(Board currBoard, int depth, Color color) {
        assert depth > 0;
        reset();
        ScoreResult scoreResult = negaMaximize(currBoard, depth, color);
        return scoreResult.bestMove;
    }

    private void reset() {
        nodesVisited = 0;
    }

    private ScoreResult negaMaximize(Board currBoard, int depth, Color color) {
        Move bestmove = null;

        List<Move> moves = generator.generate(currBoard, color);
        int max = Integer.MIN_VALUE;
        for (Move move : moves) {
            Move undoingMove = currBoard.move(move);
            int score = -negaMax(currBoard, depth - 1, color == WHITE ? BLACK : WHITE);
            if (score > max) {
                max = score;
                bestmove = move;
            }
            currBoard.move(undoingMove);
        }
        return new ScoreResult(bestmove, max);
    }

    private int negaMax(Board currBoard, int depth, Color color) {
        nodesVisited++;
        if (depth == 0)
            return evaluate.eval(currBoard, color);
        ScoreResult scoreResult = negaMaximize(currBoard, depth, color);
        return scoreResult.score;
    }
}
