package org.mattlang.jc.engine;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.NegaMaxResult;

public interface AlphaBetaSearchMethod extends SearchMethod, StatisticsCollector {

    void reset();

    Move getSavedMove();

    int getNodesVisited();

    MoveList generateMoves(BoardRepresentation currBoard, Color color);

    NegaMaxResult searchWithScore(GameState gameState, int currdepth, int alphaStart, int betaStart, MoveList moves, long stopTime);

    int getSavedMoveScore();
}