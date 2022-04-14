package org.mattlang.jc.engine.search;

import java.util.ArrayList;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.uci.GameContext;

/**
 * Exception thrown during a search.
 * Contains several information about the search to print to log file.
 */
public class SearchException extends RuntimeException {

    private GameState gameState;
    private GameContext gameContext;
    private ArrayList<IterativeDeepeningPVS.IterativeRoundResult> rounds;
    private String ebfreport;
    private Exception e;

    public SearchException(GameState gameState, GameContext gameContext,
            ArrayList<IterativeDeepeningPVS.IterativeRoundResult> rounds, String ebfreport, Exception e) {
        super(e);
        this.gameState = gameState;
        this.gameContext = gameContext;
        this.rounds = rounds;
        this.ebfreport = ebfreport;
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
        Factory.getDefaults().log(b);
        b.append("\n");

        return b.toString();
    }
}
