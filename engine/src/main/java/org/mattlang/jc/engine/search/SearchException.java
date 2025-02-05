package org.mattlang.jc.engine.search;

import java.util.ArrayList;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.uci.GameContext;

/**
 * Exception thrown during a search.
 * Contains several information about the search to print to log file.
 */
public class SearchException extends RuntimeException {

    private final SearchParameter searchParameter;
    private final GameState gameState;
    private final GameContext gameContext;
    private ArrayList<IterativeDeepeningPVS.IterativeRoundResult> rounds;
    private String ebfreport;
    private Throwable e;

    public SearchException(SearchParameter searchParameter, GameState gameState, GameContext gameContext,
            ArrayList<IterativeDeepeningPVS.IterativeRoundResult> rounds, String ebfreport, Throwable e) {
        super(e);
        this.gameState = gameState;
        this.gameContext = gameContext;
        this.rounds = rounds;
        this.ebfreport = ebfreport;
        this.searchParameter = searchParameter;
        this.e = e;
    }

    public String toStringAllInfos() {
        StringBuilder b = new StringBuilder();

        b.append("Error during Search: " + e.toString()).append("\n");
        b.append("Fen: " + gameState.getFenStr()).append("\n");
        b.append(gameState.getBoard().toUniCodeStr()).append("\n");

        b.append("\n Rounds of this game state so far:\n");

        for (IterativeDeepeningPVS.IterativeRoundResult round : rounds) {
            if (round.getRslt() != null) {
                b.append(round.getRslt().toString()).append("\n");
            }
        }
        b.append("\n");
        searchParameter.log(b);
        b.append("\n");

        return b.toString();
    }
}
