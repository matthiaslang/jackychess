package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
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

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

public class NegaMax implements SearchMethod {

    private EvaluateFunction evaluate;

    private LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    // statistics
    private int nodesVisited = 0;

    public NegaMax(EvaluateFunction evaluate) {
        this.evaluate = evaluate;
    }

    public Move search(GameState gameState, int depth) {
        assert depth > 0;
        reset();
        MoveScore scoreResult = negaMaximize(gameState.getBoard(), depth, gameState.getWho2Move());
        return scoreResult.move;
    }

    private void reset() {
        nodesVisited = 0;
        generator = Factory.getDefaults().legalMoveGenerator.create();
    }

    private MoveScore negaMaximize(BoardRepresentation currBoard, int depth, Color color) {
        Move bestmove = null;

        MoveList moves = generator.generate(currBoard, color);
        if (moves.size() == 0) {
            // no more legal moves, that means we have checkmate:
            return new MoveScore(null, - MaterialNegaMaxEval.KING_WHEIGHT);
        }
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

    private int negaMax(BoardRepresentation currBoard, int depth, Color color) {
        nodesVisited++;
        if (depth == 0)
            return evaluate.eval(currBoard, color);
        MoveScore scoreResult = negaMaximize(currBoard, depth, color);
        return scoreResult.score;
    }
}
