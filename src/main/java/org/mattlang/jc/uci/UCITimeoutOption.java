package org.mattlang.jc.uci;

import java.util.Map;

public class UCITimeoutOption extends UCISpinOption {

    public UCITimeoutOption(Map<String, UCIOption> optionBundle) {
        super(optionBundle, "thinktime", 1000, 600000, 15000);
    }

}
