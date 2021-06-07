package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Move;

import lombok.Getter;

@Getter
public class IterativeSearchResult {

    private Move savedMove;
    private NegaMaxResult rslt;

    public IterativeSearchResult(Move savedMove, NegaMaxResult rslt) {
        this.savedMove = savedMove;
        this.rslt = rslt;
    }
}
