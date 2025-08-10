package org.mattlang.jc.uci;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BestmoveExpectation {

    private String bestMove;
    private String ponderMove;

    public static final BestmoveExpectation bestmoveWithPonder(String bestMove) {
        return new BestmoveExpectation(bestMove, null);
    }

    public static final BestmoveExpectation bestmoveWithPonder(String bestMove, String ponderMove) {
        return new BestmoveExpectation(bestMove, ponderMove);
    }

    public String toUCIStringExpectation() {
        String expectation = "bestmove " + bestMove;
        if (ponderMove != null) {
            expectation += " ponder " + ponderMove;
        }
        return expectation;
    }
}
