package org.mattlang.jc;

import org.mattlang.jc.uci.UCI;

public class Logger {

    public static void log(String str, Object... args) {
        UCI.instance.putCommand("info " + String.format(str, args));
    }
}
