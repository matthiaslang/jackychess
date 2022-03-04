package org.mattlang.jc.uci;

import java.util.HashMap;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTCacheInterface;

import lombok.Getter;

/**
 * Represents the "context" of a uci game. The context is recreated each time a ucinewgame command gets send
 * to the engine.
 * The context can be used by individual per move configured factorized classes like search methods to hold state during
 * a game.
 */
public class GameContext {

    @Getter
    public TTCacheInterface ttCache;

    private HashMap<String, Object> context = new HashMap<>();

    public GameContext() {
        ttCache = new TTCache();
    }

    public GameContext(ConfigValues configValues) {
        ttCache = configValues.cacheImpls.getValue().createSupplier().get();
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
}
