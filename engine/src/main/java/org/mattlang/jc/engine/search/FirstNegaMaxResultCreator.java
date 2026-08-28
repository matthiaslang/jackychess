package org.mattlang.jc.engine.search;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.uci.GoParameter;
import org.mattlang.jc.util.MoveValidator;

public class FirstNegaMaxResultCreator {

    private final MoveValidator moveValidator = new MoveValidator();


    public NegaMaxResult createFirstNegaMaxResult(GameState gameState, SearchParameter searchParams) {
        final MoveList legalMovesToSearch = searchParams.getLegalMovesToSearch() != null ? searchParams.getLegalMovesToSearch() : moveValidator.createLegalMovesToSearch(gameState, null);
        return new NegaMaxResult(moveValidator.findSimpleBestMove(gameState, legalMovesToSearch));
    }

    public IterativeRoundResult createFirstIRR(int workerNumber, GameState gameState, SearchParameter searchParams) {
        return new IterativeRoundResult(workerNumber, createFirstNegaMaxResult(gameState, searchParams), new StopWatch());
    }
}
