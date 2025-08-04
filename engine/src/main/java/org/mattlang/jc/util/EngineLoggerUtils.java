package org.mattlang.jc.util;

import static org.mattlang.jc.util.LoggerUtils.formatSevere;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;

public class EngineLoggerUtils {

    public static String fmtSevere(SearchParameter searchParameter, GameState gameState, String msg) {
        StringBuilder b = formatSevere(gameState, msg);
        searchParameter.log(b);
        return b.toString();
    }

}
