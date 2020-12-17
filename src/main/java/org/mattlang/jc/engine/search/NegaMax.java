package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

public class NegaMax implements SearchMethod {

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.createLegalMoveGenerator();

    // statistics
    private int nodesVisited = 0;

    public NegaMax(EvaluateFunction evaluate) {
        this.evaluate = evaluate;
    }

    public Move search(Board currBoard, int depth, Color color) {
        assert depth > 0;
        reset();
        MoveScore scoreResult = negaMaximize(currBoard, depth, color);
        return scoreResult.move;
    }

    private void reset() {
        nodesVisited = 0;
        generator = Factory.createLegalMoveGenerator();
    }

    private MoveScore negaMaximize(Board currBoard, int depth, Color color) {
        Move bestmove = null;

        MoveList moves = generator.generate(currBoard, color);
        int max = Integer.MIN_VALUE;
        for (MoveCursor moveCursor : moves) {

            moveCursor.move(currBoard);
            int score = -negaMax(currBoard, depth - 1, color == WHITE ? BLACK : WHITE);
            if (score > max) {
                max = score;
                bestmove = moveCursor.getMove();
            }
            moveCursor.undoMove(currBoard);
        }
        return new MoveScore(bestmove, max);
    }

    private int negaMax(Board currBoard, int depth, Color color) {
        nodesVisited++;
        if (depth == 0)
            return evaluate.eval(currBoard, color);
        MoveScore scoreResult = negaMaximize(currBoard, depth, color);
        return scoreResult.score;
    }
}
