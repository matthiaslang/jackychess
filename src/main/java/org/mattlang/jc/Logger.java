package org.mattlang.jc;

import org.mattlang.jc.uci.UCI;

public class Logger {

    public static void log(String str, Object... args) {
        UCI.instance.putCommand("info string " + String.format(str, args));
    }

    // info depth 4 nodes 123456
    public static void info(int depth, int nodes, int cp) {
        UCI.instance.putCommand( String.format("info depth %d nodes %d score cp %d", depth, nodes, cp));
    }
}
