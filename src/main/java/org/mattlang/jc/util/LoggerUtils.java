package org.mattlang.jc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static void logStats(Logger logger, String msg, Map<String, Object> stats) {
        if (logger.isLoggable(Level.FINE)) {
            StringWriter sw = new StringWriter();
            try (PrintWriter writer = new PrintWriter(sw)) {
                logStatsRecursive(writer, 0, stats);
            }
            logger.fine(msg + "\n" + sw.toString());
        }
    }

    private static void logStatsRecursive(PrintWriter writer, int prefix, Map<String, Object> stats) {

        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            if (entry.getValue() instanceof Map) {
                tabs(writer, prefix);
                writer.println(entry.getKey());
                logStatsRecursive(writer, prefix + 2, (Map<String, Object>) entry.getValue());
            } else {
                tabs(writer, prefix);
                writer.println(entry.getKey() + " = " + entry.getValue());
            }
        }

    }

    private static void tabs(PrintWriter writer, int prefix) {
        for (int i = 0; i < prefix; i++) {
            writer.write(" ");
        }
    }
}
