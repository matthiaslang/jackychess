package org.mattlang.jc;

import static org.mattlang.jc.util.LoggerUtils.fmtSevere;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.uci.UCI;

public class UCILogger {

    public static boolean uciDebugMode = false;

    public static void logDebug(String str, Object... args) {
        if (uciDebugMode) {
            String formattedStr = String.format(str, args);
            for (String s : formattedStr.split("\n")) {
                UCI.instance.putCommand("info string " + s);
            }
        }
    }

    public static void log(String str, Object... args) {
        UCI.instance.putCommand("info string " + String.format(str, args));
    }

    // info depth 4 nodes 123456
    public static void info(int depth, int nodes, int cp) {
        UCI.instance.putCommand(String.format("info depth %d nodes %d score cp %d", depth, nodes, cp));
    }

    public static void logDebugSevere(SearchParameter searchParams, GameState gameState, String msg, Throwable e) {
        if (uciDebugMode) {
            logDebug(msg);
            logDebug(fmtSevere(searchParams, gameState, msg));
            logDebug(e.getLocalizedMessage());
            logDebug(stacktraceToStr(e));
        }
    }

    private static String stacktraceToStr(Throwable e) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(ous);
        e.printStackTrace(ps);
        ps.close();
        return ous.toString();
    }
}
