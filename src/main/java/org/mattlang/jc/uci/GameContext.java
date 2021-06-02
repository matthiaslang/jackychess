package org.mattlang.jc.uci;

import java.util.HashMap;

import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.KillerMoves;

import lombok.Getter;

/**
 * Represents the "context" of a uci game. The context is recreated each time a ucinewgame command gets send
 * to the engine.
 * The context can be used by individual per move configured factorized classes like search methods to hold state during
 * a game.
 */
public class GameContext {

    @Getter
    private HistoryHeuristic historyHeuristic = new HistoryHeuristic();

    @Getter
    private KillerMoves killerMoves = new KillerMoves();

    private HashMap<String, Object> context = new HashMap<>();

    public <T> T getContext(String name) {
        return (T) context.get(name);
    }

    public <T> void setContext(String name, T ctx) {
        context.put(name, ctx);
    }
}
