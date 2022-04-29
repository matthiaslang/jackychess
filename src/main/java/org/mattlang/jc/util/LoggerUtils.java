package org.mattlang.jc.util;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.GameState;

public class LoggerUtils {

    public static String fmtSevere(GameState gameState, String msg) {
        StringBuilder b = new StringBuilder();

        b.append(msg).append("\n");

        b.append("FEN POS: ").append(gameState.getFenStr()).append("\n");

        b.append("Board: \n" + gameState.getBoard().toUniCodeStr() + "\n");
        Factory.getDefaults().log(b);

        return b.toString();
    }
}
