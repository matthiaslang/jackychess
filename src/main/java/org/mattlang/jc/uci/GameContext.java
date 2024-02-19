package org.mattlang.jc.uci;

import java.util.HashMap;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.evaluation.parameval.EvalCache;
import org.mattlang.jc.engine.search.SearchStatistics;
import org.mattlang.jc.engine.tt.Caching;
import org.mattlang.jc.engine.tt.TTCache;

import lombok.Getter;

/**
 * Represents the "context" of an uci game. The context is recreated each time an ucinewgame command gets send
 * to the engine.
 * The context can be used by individual per move configured factorized classes like search methods to hold state during
 * a game.
 */
public class GameContext {

    private static final Logger LOGGER = Logger.getLogger(GameContext.class.getSimpleName());

    @Getter
    public TTCache ttCache;

    private HashMap<String, Object> context = new HashMap<>();

    private SearchStatistics statistics = new SearchStatistics();

    public GameContext() {
        ttCache = Caching.CACHING.getTtCache();
        ttCache.reset();
        System.gc();
    }

    public GameContext(ConfigValues configValues) {
        ttCache = Caching.CACHING.getTtCache();
        ttCache.reset();
        EvalCache.instance.reset();
        System.gc();
    }

    public <T> T getContext(String name) {
        return (T) context.get(name);
    }

    public <T> void setContext(String name, T ctx) {
        context.put(name, ctx);
    }

    /**
     * To do any initialisations before the next ply the engine should do.
     *
     * @param gameState
     */
    public void initNewMoveSearch(GameState gameState) {

        /** update aging of tt cache. */
        ttCache.updateAging(gameState.getBoard());
    }

    public void addStatistics(SearchStatistics statistics) {
        this.statistics.add(statistics);
    }

    public void logStatistics() {
        statistics.logStatistics();
    }
}
